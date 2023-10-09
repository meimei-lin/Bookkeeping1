package com.example.bookkeeping.utils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.bookkeeping.R;

public class KeyBoardUtils {
    private final Keyboard k1; //自定義鍵盤
    private KeyboardView keyboardView;
    private EditText editText;
    private KeyBoardUtils.OnEnsureListener OnEnsureListener;

    public interface OnEnsureListener{
        public void onEnsure();
    }

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.OnEnsureListener = onEnsureListener;
    }

    public KeyBoardUtils(KeyboardView keyboardView, EditText editText) {
        this.keyboardView = keyboardView;
        this.editText = editText;
        this.editText.setInputType(InputType.TYPE_NULL);//取消彈出系統鍵盤
        k1 =new Keyboard(this.editText.getContext(), R.xml.key);

        this.keyboardView.setKeyboard(k1); //設置要顯示鍵盤的樣式
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(false);
        this.keyboardView.setOnKeyboardActionListener(listener);
    }

    KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();
            switch (primaryCode){
                case Keyboard.KEYCODE_DELETE: //點擊刪除
                    if(editable!=null && editable.length()>0){
                        if(start>0){
                            editable.delete(start-1,start);
                        }
                    }
                    break;
                case Keyboard.KEYCODE_CANCEL: //點擊清除
                    editable.clear();
                    break;
                case Keyboard.KEYCODE_DONE: //點擊確定
                    OnEnsureListener.onEnsure(); // 通過interface回調的方法，當點擊確定時，可以調用該方法
                    break;
                default: //其他數字直接插入
                    editable.insert(start,Character.toString((char)primaryCode));
                    break;
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };
    //顯示鍵盤的方法
    public void showKeyboard(){
        int visibility = keyboardView.getVisibility();
        if(visibility == View.INVISIBLE || visibility==View.GONE){
            keyboardView.setVisibility(View.VISIBLE);
        }
    }
    //隱藏鍵盤
    public void hideKeyboard(){
        int visibility = keyboardView.getVisibility();
        if(visibility == View.VISIBLE || visibility == View.INVISIBLE){
            keyboardView.setVisibility(View.GONE);
        }
    }
}
