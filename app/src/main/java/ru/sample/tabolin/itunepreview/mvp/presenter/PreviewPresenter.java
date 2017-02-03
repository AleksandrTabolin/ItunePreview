package ru.sample.tabolin.itunepreview.mvp.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.sample.tabolin.itunepreview.mvp.view.PreviewView;
import ru.sample.tabolin.itunepreview.web.WebApi;


@InjectViewState
public class PreviewPresenter extends MvpPresenter<PreviewView> {

    private Disposable loadPreviewListDisposable;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().init();
    }

    public void loadPreviewListByKeyWord(String keyWord){
        if (loadPreviewListDisposable != null){
            loadPreviewListDisposable.dispose();
        }

        getViewState().showLoading(true);
        loadPreviewListDisposable = WebApi.get()
                .loadPreviewListByKeyWord(keyWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (previewModels) -> {
                            getViewState().showLoading(false);
                            getViewState().setPreviewList(previewModels);
                        },
                        (throwable) -> {
                            getViewState().showLoading(false);
                            getViewState().previewLoadingError(throwable);
                        }
                );
    }

    @Override
    public void onDestroy() {
        if (loadPreviewListDisposable != null){
            loadPreviewListDisposable.dispose();
        }
        super.onDestroy();
    }
}
