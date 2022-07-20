package com.mx.ymate.dev.result;

import com.mx.ymate.dev.view.MxJsonView;
import net.ymate.platform.webmvc.IWebResult;

import java.io.Serializable;

public interface IMxWebResult<CODE_TYPE extends Serializable> extends IWebResult<CODE_TYPE> {

    MxJsonView toMxJsonView();

    MxJsonView toMxJsonView(String callback);

    IMxWebResult<CODE_TYPE> withContentType();
}
