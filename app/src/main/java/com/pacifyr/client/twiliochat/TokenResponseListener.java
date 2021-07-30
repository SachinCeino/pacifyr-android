package com.pacifyr.client.twiliochat;

import org.jetbrains.annotations.Nullable;

public interface TokenResponseListener {
    void receivedTokenResponse(boolean success, @Nullable Exception exception, String token);
}
