package com.mygame.android.net.handle;

import com.mygame.android.model.Model;
import com.mygame.android.net.NetRequest;

public interface INetResponseDataParse {

	<T> Model<T> responseDataParse(NetRequest request,String result,Class responseClass) throws Exception;
	
}
