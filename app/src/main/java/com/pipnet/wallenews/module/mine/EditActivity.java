package com.pipnet.wallenews.module.mine;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.matisse.GifSizeFilter;
import com.pipnet.wallenews.matisse.Glide4Engine;
import com.pipnet.wallenews.util.FileUtils;
import com.pipnet.wallenews.util.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.List;

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

    private static final int REQUEST_CODE_CHOOSE = 1001;

    @Override
    public int setContentView() {
        return R.layout.activity_edit;
    }

    @Override
    public void initViewData() {
        title.setText("编辑个人资料");
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
            upLoadImg(FileUtils.imageToBase64(path));
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

    private void upLoadImg(String path) {
        File file = new File(path);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/png"), file);
        builder.addFormDataPart("file[]", "avatar.png", photoRequestBody);
        List<MultipartBody.Part> parts = builder.build().parts();
        NetRequest.uploadImg(parts, new BaseSubscriber<Response>() {
            @Override
            public void onNext(Response response) {

            }
        });
    }
}
