package com.mygame.android.net.handle;

import com.mygame.android.model.DefaultModel;
import com.mygame.android.net.NetRequest;

public class DefaultNetResponseDataParse implements INetResponseDataParse {

	@Override
	public DefaultModel responseDataParse(NetRequest request, String result,Class responseClass) throws Exception {
		// TODO Auto-generated method stub
		DefaultModel response = new DefaultModel();
		response.setData(result);
		return response;
	}

}
