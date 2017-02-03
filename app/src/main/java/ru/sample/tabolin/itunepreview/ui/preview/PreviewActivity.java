package ru.sample.tabolin.itunepreview.ui.preview;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ru.sample.tabolin.itunepreview.R;
import ru.sample.tabolin.itunepreview.domain.PreviewModel;
import ru.sample.tabolin.itunepreview.mvp.presenter.PreviewPresenter;
import ru.sample.tabolin.itunepreview.mvp.view.PreviewView;
import ru.sample.tabolin.itunepreview.util.rx.RxTextView;

public class PreviewActivity extends MvpAppCompatActivity implements PreviewView {

    enum ListAppearance{
        LIST, GRID
    }

    private final static String TAG = "PreviewActivity";

    @BindView(R.id.activity_preview_search) EditText searchView;
    @BindView(R.id.activity_preview_data_list) RecyclerView listView;
    @BindView(R.id.activity_preview_progress) CircularProgressView progressView;
    @BindView(R.id.activity_preview_change_list_appearance) ImageView listAppearanceButton;

    private PreviewListAdapter listAdapter;

    @InjectPresenter PreviewPresenter previewPresenter;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @State String keyWord;
    @State ListAppearance listAppearance = ListAppearance.LIST;

    @Override
    public void init() {
        previewPresenter.loadPreviewListByKeyWord(keyWord);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);
        Icepick.restoreInstanceState(this, savedInstanceState);

        listAdapter = new PreviewListAdapter(this);
        listView.setAdapter(listAdapter);
        setListAppearance(listView);

        searchView.setText(keyWord);
        bindSearchView(searchView);
    }

    private void setListAppearance(RecyclerView listView){
        int iconRes = listAppearance == ListAppearance.LIST
                ? R.drawable.ic_view_module : R.drawable.ic_view_stream;

        listAppearanceButton.setImageDrawable(ContextCompat.getDrawable(this, iconRes));

        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        int columnCount = isPortrait ? 2 : 3;

        listView.setLayoutManager(listAppearance == ListAppearance.LIST
                ? new LinearLayoutManager(this)
                : new GridLayoutManager(this, columnCount)
        );
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    private void bindSearchView(EditText searchView){
        Disposable disposable = RxTextView
                .afterTextChanged(searchView)
                .filter(keyWord -> keyWord.length() >= 5)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(s -> keyWord = s)
                .subscribe(keyWord -> previewPresenter.loadPreviewListByKeyWord(keyWord));
        compositeDisposable.add(disposable);
    }

    @OnClick(R.id.activity_preview_change_list_appearance)
    void onChangeAppearanceClick(){
        listAppearance = listAppearance == ListAppearance.LIST
                ? ListAppearance.GRID : ListAppearance.LIST;
        setListAppearance(listView);
    }

    @Override
    public void setPreviewList(List<PreviewModel> previewList) {
        listAdapter.setItems(previewList);
    }

    @Override
    public void showLoading(boolean show) {
        progressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void previewLoadingError(Throwable throwable) {
        Log.e(TAG, "previewLoadingError", throwable);
        Toast.makeText(this, "Something went wrong. Try again.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        progressView.stopAnimation();
        super.onDestroy();
    }
}
