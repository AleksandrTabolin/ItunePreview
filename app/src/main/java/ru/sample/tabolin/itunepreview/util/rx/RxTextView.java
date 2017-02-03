package ru.sample.tabolin.itunepreview.util.rx;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

public class RxTextView {

    public static <T extends TextView> Observable<String> afterTextChanged(final T view) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                TextWatcher watcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        e.onNext(s.toString());
                    }
                };

                view.addTextChangedListener(watcher);
                e.setDisposable(new Disposable() {
                                    private boolean isDisposed = false;

                                    @Override
                                    public void dispose() {
                                        view.removeTextChangedListener(watcher);
                                        isDisposed = true;
                                    }

                                    @Override
                                    public boolean isDisposed() {
                                        return isDisposed;
                                    }
                                }
                );
            }
        });


    }

}
