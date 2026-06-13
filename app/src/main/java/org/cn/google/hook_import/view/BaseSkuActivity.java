package org.cn.google.hook_import.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;

import org.cn.google.app.ProviderBridge;
import org.cn.google.mode.SkuDetailsModel;
import org.cn.google.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

public class BaseSkuActivity extends Activity implements AdapterView.OnItemClickListener {

    public LinearLayout mRootLinearLayout;
    public EditText mSearchEditText;
    public TextView mStatusTextText, mHintTextView;
    public ListView mSkuListView;

    protected SkuListAdapter mSkuListAdapter;
    protected List<SkuDetailsModel> skuDetailsModels = new ArrayList();
    protected List<SkuDetailsModel> skuDetailsModelsSearch = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRootView();
        initHeadsView();
        initSkuListView();
        initHintTextView();
        getProductData();
        rxSearchEditTextChanged();
    }

    private void initRootView() {
        mRootLinearLayout = new LinearLayout(this);
        mRootLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mRootLinearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(-1, -1);
        mRootLinearLayout.setLayoutParams(layoutParams);
        setContentView(mRootLinearLayout);
    }

    private void initSkuListView() {
        mSkuListView = new ListView((Context) this);
        mSkuListAdapter = new SkuListAdapter((Context) this, skuDetailsModels);
        mSkuListView.setAdapter(mSkuListAdapter);
        mSkuListView.setOnItemClickListener(this);
        mSkuListView.setVisibility(View.GONE);
        mRootLinearLayout.addView(mSkuListView);
    }

    private void initHintTextView() {
        mHintTextView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        layoutParams.setMargins(10, 10, 10, 10);
        mHintTextView.setLayoutParams(layoutParams);
        mHintTextView.setTextColor(Color.BLACK);
        mHintTextView.setTextSize(13f);
        mHintTextView.setGravity(Gravity.CENTER);
        mRootLinearLayout.addView(mHintTextView);
    }

    private void initHeadsView() {
        LinearLayout linearLayout1 = new LinearLayout(this);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams1 =
                new LinearLayout.LayoutParams(-1, ConvertUtils.dp2px(40));
        layoutParams1.setMargins(ConvertUtils.dp2px(10), ConvertUtils.dp2px(5), ConvertUtils.dp2px(10), ConvertUtils.dp2px(5));
        linearLayout1.setLayoutParams(layoutParams1);

        mStatusTextText = new TextView(this);
        GradientDrawable gradientDrawable1 = new GradientDrawable();
        gradientDrawable1.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable1.setCornerRadius(10f);
        gradientDrawable1.setColor(Color.parseColor("#000000"));
        LinearLayout.LayoutParams layoutParams2 =
                new LinearLayout.LayoutParams(ConvertUtils.dp2px(120), ConvertUtils.dp2px(40));
        layoutParams2.rightMargin = ConvertUtils.dp2px(10);
        mStatusTextText.setLayoutParams(layoutParams2);
        mStatusTextText.setBackground(gradientDrawable1);
        mStatusTextText.setText("GOOGLE TRUE");
        mStatusTextText.setTextColor(Color.WHITE);
        mStatusTextText.setTextSize(13f);
        mStatusTextText.setGravity(Gravity.CENTER);
        mStatusTextText.setSingleLine(true);
        mStatusTextText.setPadding(ConvertUtils.dp2px(5), 0, ConvertUtils.dp2px(5), 0);

        mSearchEditText = new EditText(this);
        GradientDrawable gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable2.setCornerRadius(10f);
        gradientDrawable2.setColor(Color.parseColor("#CCCCCC"));
        LinearLayout.LayoutParams layoutParams3 =
                new LinearLayout.LayoutParams(-1, ConvertUtils.dp2px(40));
        mSearchEditText.setLayoutParams(layoutParams3);
        mSearchEditText.setBackground(gradientDrawable2);
        mSearchEditText.setHint("请输入关键字搜索...");
        mSearchEditText.setTextColor(Color.parseColor("#333333"));
        mSearchEditText.setTextSize(13f);
        mSearchEditText.setGravity(Gravity.CENTER_VERTICAL);
        mSearchEditText.setSingleLine(true);
        mSearchEditText.setPadding(ConvertUtils.dp2px(10), 0, ConvertUtils.dp2px(10), 0);

        linearLayout1.addView(mStatusTextText);
        linearLayout1.addView(mSearchEditText);

        mRootLinearLayout.addView(linearLayout1);
    }

    public void setGoogleStart(Boolean isConnected) {
        GradientDrawable gradientDrawable1 = new GradientDrawable();
        gradientDrawable1.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable1.setCornerRadius(10f);
        if (isConnected) {
            gradientDrawable1.setColor(Color.parseColor("#000000"));
            mStatusTextText.setText("GOOGLE TRUE");
        } else {
            gradientDrawable1.setColor(Color.parseColor("#999999"));
            mStatusTextText.setText("GOOGLE FALSE");
        }
        mStatusTextText.setBackground(gradientDrawable1);
    }

    public void getProductData() {
        mHintTextView.setText("正在加载中...");
        Bundle bundle = new Bundle();
        bundle.putString("packageName", getPackageName());
        Flowable.just(bundle).map(new Function<Bundle, Bundle>() {
            @Override
            public Bundle apply(Bundle bundle) throws Throwable {
                Bundle bundle1 = ProviderBridge.httpGetSkuList((Context) BaseSkuActivity.this, bundle);
                if (!ProviderBridge.checkResultCode(bundle1))
                    throw new Exception(ProviderBridge.getResultMsg(bundle1));
                return bundle1;
            }
        })
                .map(new Function<Bundle, List<SkuDetailsModel>>() {
                    @Override
                    public List<SkuDetailsModel> apply(Bundle bundle) throws Throwable {
                        String skuString = bundle.getString("data");
                        if (skuString == null)
                            throw new Exception("sku列表为空");
                        List<SkuDetailsModel> skuDetailsModels = JsonUtils.stringToList(skuString, SkuDetailsModel.class);
                        if (skuDetailsModels.size() == 0)
                            throw new Exception("sku列表为空");
                        return skuDetailsModels;
                    }
                })
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<SkuDetailsModel>>() {
                    @Override
                    public void onNext(List<SkuDetailsModel> skuDetailsModels) {
                        mSkuListView.setVisibility(View.VISIBLE);
                        mSkuListAdapter.setNewInstance(skuDetailsModels);
                        skuDetailsModelsSearch.clear();
                        skuDetailsModelsSearch.addAll(skuDetailsModels);
                    }

                    @Override
                    public void onError(Throwable t) {
                        mHintTextView.setText("异常：" + t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        mHintTextView.setVisibility(View.GONE);
                        mHintTextView.setText("加载完成");
                    }
                });
    }

    private void rxSearchEditTextChanged() {
        RxTextView.textChanges(mSearchEditText).skip(1)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<CharSequence>() {
                    @Override
                    public void onNext(CharSequence value) {
                        if (value.length() == 0) {
                            mSkuListAdapter.setNewInstance(skuDetailsModelsSearch);
                        } else {
                            List<SkuDetailsModel> sku = new ArrayList<>();
                            for (SkuDetailsModel skuDetailsModel : skuDetailsModelsSearch) {
                                if (skuDetailsModel.getTitle().contains(value)) {
                                    sku.add(skuDetailsModel);
                                }
                            }
                            mSkuListAdapter.setNewInstance(sku);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
