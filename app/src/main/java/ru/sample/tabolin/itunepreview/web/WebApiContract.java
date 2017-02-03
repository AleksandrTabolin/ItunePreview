package ru.sample.tabolin.itunepreview.web;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.sample.tabolin.itunepreview.domain.PreviewModel;
import ru.sample.tabolin.itunepreview.web.response.LoadPreviewListResponse;

public interface WebApiContract {

    @GET("search/?media=music")
    Single<LoadPreviewListResponse> loadPreviewListByKeyWord(@Query("term") String keyWord);
}
