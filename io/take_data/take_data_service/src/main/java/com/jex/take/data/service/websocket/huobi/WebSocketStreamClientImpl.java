package com.jex.take.data.service.websocket.huobi;

import com.jex.take.data.service.enums.CandlestickInterval;
import com.jex.take.data.service.model.event.CandlestickEvent;
import com.jex.take.data.service.model.event.CandlestickReqEvent;
import com.jex.take.data.service.util.SubscriptionErrorHandler;
import com.jex.take.data.service.util.SubscriptionListener;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class WebSocketStreamClientImpl implements SubscriptionClient {

    private final SubscriptionOptions options;
    private WebSocketWatchDog watchDog;

    private final WebsocketRequestImpl requestImpl;

    private final List<WebSocketConnection> connections = new LinkedList<>();


    public WebSocketStreamClientImpl(SubscriptionOptions options) {

        this.watchDog = null;
        this.options = Objects.requireNonNull(options);

        this.requestImpl = new WebsocketRequestImpl();
    }

    private <T> void createConnection(WebsocketRequest<T> request, boolean autoClose) {
        if (watchDog == null) {
            watchDog = new WebSocketWatchDog(options);
        }
        WebSocketConnection connection = new WebSocketConnection(
                options, request, watchDog, autoClose);
        if (autoClose == false) {
            connections.add(connection);
        }
        connection.connect();
    }

    private <T> void createConnection(WebsocketRequest<T> request) {
        createConnection(request, false);
    }

    private List<String> parseSymbols(String symbol) {
        return Arrays.asList(symbol.split("[,]"));
    }

    @Override
    public void subscribeCandlestickEvent(
            String symbols,
            CandlestickInterval interval,
            SubscriptionListener<CandlestickEvent> callback) {
        subscribeCandlestickEvent(symbols, interval, callback, null);
    }

    @Override
    public void subscribeCandlestickEvent(
            String symbols,
            CandlestickInterval interval,
            SubscriptionListener<CandlestickEvent> subscriptionListener,
            SubscriptionErrorHandler errorHandler) {
        createConnection(requestImpl.subscribeCandlestickEvent(
                parseSymbols(symbols), interval, subscriptionListener, errorHandler));
    }

    @Override
    public void requestCandlestickEvent(
            String symbols, Long from, Long to,
            CandlestickInterval interval,
            SubscriptionListener<CandlestickReqEvent> subscriptionListener) {
        requestCandlestickEvent(symbols, from, to, interval, subscriptionListener, null);
    }

    @Override
    public void requestCandlestickEvent(
            String symbols, Long from, Long to,
            CandlestickInterval interval,
            SubscriptionListener<CandlestickReqEvent> subscriptionListener,
            SubscriptionErrorHandler errorHandler) {
        requestCandlestickEvent(symbols, from, to, interval, true, subscriptionListener, errorHandler);
    }

    public void requestCandlestickEvent(
            String symbols, Long from, Long to,
            CandlestickInterval interval,
            boolean autoClose,
            SubscriptionListener<CandlestickReqEvent> subscriptionListener,
            SubscriptionErrorHandler errorHandler) {
        createConnection(requestImpl.requestCandlestickEvent(
                parseSymbols(symbols), from, to, interval, subscriptionListener, errorHandler), autoClose);
    }
}