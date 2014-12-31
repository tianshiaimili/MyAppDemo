package com.hua.service;

/** 
 * 服务器回调接口 
 *  
 * @author way 
 *  
 */  
public interface IConnectionStatusCallback {  
    /** 
     * 连接状态改变 
     *  
     * @param connectedState 
     *            连接状态，有连接、未连接、链接中三种 
     * @param reason 
     *            连接失败的原因 
     */  
    public void connectionStatusChanged(int connectedState, String reason);  
} 
