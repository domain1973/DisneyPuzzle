package com.ads.puzzle.disnep.android;

/**
 * Created by Administrator on 2014/9/11.
 */
public interface ActionResolver {
    public void showToast(CharSequence toastMessage, int toastDuration);
    public void showAlertBox(String alertBoxTitle, String alertBoxMessage, String alertBoxButtonText);
    public void openUri(String uri);
    public void showMyList();
}
