package com.selectors;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ChatServer {
    public static void main(String[] args) {
        try {
            // Create a ServerSocketChannel
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress("localhost", 5000));
            serverSocketChannel.configureBlocking(false);

            // Create a Selector
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Chat server started on port 5000...");

            while (true) {
                // Wait for events (until one of the channels is ready)
                selector.select();

                // Get the keys that are ready for operation
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    // If a new connection is ready to be accepted
                    if (key.isAcceptable()) {
                        handleAccept(serverSocketChannel, selector);
                    }

                    // If data is ready to be read
                    if (key.isReadable()) {
                        handleRead(key);
                    }

                    // Remove the current key to avoid reprocessing
                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle a new connection
    private static void handleAccept(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);

        // Register the channel for reading events
        socketChannel.register(selector, SelectionKey.OP_READ);

        System.out.println("New client connected: " + socketChannel.getRemoteAddress());
    }

    // Handle reading data
    private static void handleRead(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(256);
        int bytesRead = socketChannel.read(buffer);

        // If connection is closed
        if (bytesRead == -1) {
            socketChannel.close();
            System.out.println("Client disconnected.");
        } else {
            String message = new String(buffer.array()).trim();
            // Send the message back to the client
            System.out.println("Received message: " + message);


            buffer.flip();
            socketChannel.write(buffer);
        }
    }
}
