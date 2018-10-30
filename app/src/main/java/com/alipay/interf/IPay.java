package com.alipay.interf;

import com.alipay.PayResult;

/**
 * 支付的回调接口
 * @author Administrator
 *
 */
public interface IPay {
	/**
	 * 支付成功
	 */
	void onSuccess(String msg);

	/**
	 * 支付失败
	 * @param payResult
     */
	void onFailure(PayResult payResult);
}
