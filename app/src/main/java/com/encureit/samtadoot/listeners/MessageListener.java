/*
 * Copyright (c) - 2020 & Created By AbhishekR - EncureIT :)
 */

package com.encureit.samtadoot.listeners;

public interface MessageListener {
    /**
     * To call this method when new message received and send back
     *
     * @param message Message
     */
    void messageReceived(String message);
}
