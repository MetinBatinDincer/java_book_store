package com.bookstore.ui.view;

import com.bookstore.ui.auth.SessionManager;
import javafx.scene.layout.BorderPane;

public abstract class BaseView extends BorderPane {

    protected final SessionManager session = SessionManager.getInstance();

    protected abstract void buildUI();
    public abstract void refresh();
}
