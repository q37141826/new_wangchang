package com.qixiu.newoulingzhu.mvp.view.activity.base.upload;

import android.os.AsyncTask;


/**
 * Created by Administrator on 2017/7/3 0003.
 */

public class AsyncTaskFactory {


    public static AsyncTask CreateDefaultAsyncTask(AsyncTaskInterface asyncTaskInterface) {
        return new DefaultAsyncTask(asyncTaskInterface);
    }

    public interface AsyncTaskInterface<Params, Progress, Result> {
        Result doInBackground(Params... params);

        void onPostExecute(Result result);

        void onPreExecute();

        void onProgressUpdate(Progress... values);

    }

    private static class DefaultAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
        private AsyncTaskInterface<Params, Progress, Result> asyncTaskInterface;

        public DefaultAsyncTask(AsyncTaskInterface<Params, Progress, Result> asyncTaskInterface) {
            this.asyncTaskInterface = asyncTaskInterface;
        }

        @Override
        protected Result doInBackground(Params... params) {

            return asyncTaskInterface.doInBackground(params);
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            asyncTaskInterface.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asyncTaskInterface.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Progress... values) {
            super.onProgressUpdate(values);
            asyncTaskInterface.onProgressUpdate(values);
        }
    }

}
