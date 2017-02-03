package ru.sample.tabolin.itunepreview.mvp.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.sample.tabolin.itunepreview.domain.PreviewModel;

public interface PreviewView extends MvpView {

    @StateStrategyType(SkipStrategy.class)
    void init();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setPreviewList(List<PreviewModel> previewList);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showLoading(boolean show);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void previewLoadingError(Throwable throwable);
}
