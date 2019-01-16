package com.pipnet.wallenews.module.mine;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.base.Constans;
import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.bean.UploadResponse;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.Router;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.matisse.GifSizeFilter;
import com.pipnet.wallenews.matisse.Glide4Engine;
import com.pipnet.wallenews.util.SPUtils;
import com.pipnet.wallenews.util.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by LeeBoo on 2019/1/14.
 */

public class EditActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.avatar)
    SimpleDraweeView avatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_intro)
    TextView tvIntro;

    LoginInfo info;

    private static final int REQUEST_CODE_CHOOSE = 1001;

    @Override
    public int setContentView() {
        return R.layout.activity_edit;
    }

    @Override
    public void initViewData() {
        EventBus.getDefault().register(this);
        title.setText("编辑个人资料");
        info = SPUtils.getObject(LoginInfo.class);
        initUserInfo(info);
    }

    private void initUserInfo(LoginInfo info) {
        tvName.setText(TextUtils.isEmpty(info.nickName) ? "" : info.nickName);
        tvIntro.setText(TextUtils.isEmpty(info.introduction) ? "" : info.introduction);
        if (!TextUtils.isEmpty(info.avatar)) {
            avatar.setImageURI(info.avatar);
        } else {
            avatar.setImageResource(R.mipmap.default_avatar);
        }
    }

    @OnClick({R.id.btn_left, R.id.rl_avatar, R.id.rl_name, R.id.intro})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.rl_avatar:
                openPhotoCheckPermission();
                break;
            case R.id.rl_name:
                startActivity(new Intent(EditActivity.this, EditNameActivity.class));
                break;
            case R.id.intro:
                startActivity(new Intent(EditActivity.this, EditIntroActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            String path = Matisse.obtainPathResult(data).get(0);
            upLoadImg(path);
        }
    }

    private void openPhotoCheckPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            Matisse.from(EditActivity.this)
                                    .choose(MimeType.ofImage())
                                    .theme(R.style.Matisse_Zhihu)
                                    .capture(true)
                                    .captureStrategy(
                                            new CaptureStrategy(true, "com.pipnet.wallenews.fileprovider", "test"))
                                    .countable(false)
                                    .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                                    .maxSelectable(1)
                                    .spanCount(4)
                                    .originalEnable(false)
                                    .maxOriginalSize(10)
                                    .imageEngine(new Glide4Engine())
                                    .forResult(REQUEST_CODE_CHOOSE);

                        } else {
                            ToastUtil.show("请打开相册相关权限");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //上传图片
    private void upLoadImg(String path) {
        File file = new File(path);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
        builder.addFormDataPart("files[]", "avatar.png", photoRequestBody);
        List<MultipartBody.Part> parts = builder.build().parts();
        NetRequest.uploadImg(parts, new BaseSubscriber<UploadResponse>() {
            @Override
            public void onNext(UploadResponse response) {
                if (response.files != null && response.files.size() > 0) {
                    modifyAvatar(Router.BASE_URL + response.files.get(0).url);
                }
            }
        });
    }

    //修改头像
    private void modifyAvatar(final String url) {
        Map<String, String> map = new HashMap<>();
        map.put("image", url);
        NetRequest.modify(info.userId + "", map, new BaseSubscriber<Response>() {
            @Override
            public void onNext(Response response) {
                if (!TextUtils.isEmpty(response.status) && response.status.equals("OK")) {
                    ToastUtil.show("修改成功");
                    info.avatar = url;
                    SPUtils.setObject(info);
                    avatar.setImageURI(url);
                    EventBus.getDefault().post(Constans.REFRESH_USER);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String event) {
        if (event.equals(Constans.REFRESH_USER)) {
            initUserInfo(SPUtils.getObject(LoginInfo.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
