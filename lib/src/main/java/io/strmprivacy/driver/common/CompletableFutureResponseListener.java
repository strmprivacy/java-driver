package io.strmprivacy.driver.common;

import org.eclipse.jetty.client.HttpContentResponse;
import org.eclipse.jetty.client.HttpResponseException;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.client.util.BufferingResponseListener;
import org.eclipse.jetty.http.HttpStatus;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureResponseListener extends BufferingResponseListener {
    private final CompletableFuture<ContentResponse> completable;

    public CompletableFutureResponseListener(CompletableFuture<ContentResponse> completable) {
        this.completable = completable;
    }

    @Override
    public void onComplete(Result result) {
        if (result.isFailed()) {
            completable.completeExceptionally(result.getFailure());
        } else {
            Response response = result.getResponse();
            HttpContentResponse httpContentResponse = new HttpContentResponse(response, getContent(), getMediaType(), getEncoding());
            if (HttpStatus.isSuccess(response.getStatus())) {
                completable.complete(httpContentResponse);
            } else {
                completable.completeExceptionally(new HttpResponseException(this.getContentAsString(), httpContentResponse));
            }
        }
    }
}
