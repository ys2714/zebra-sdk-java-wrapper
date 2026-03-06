package com.zebra.zsdk_java_wrapper.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FixedSizeQueue<T extends FixedSizeQueueItem> {

    private final int size;
    private final ArrayList<T> items;

    /**
     * Constructor for the FixedSizeQueue.
     * @param size The maximum size of the queue. Must be greater than 0.
     * @throws IllegalArgumentException if size is not greater than 0.
     */
    public FixedSizeQueue(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size must be greater than 0");
        }
        this.size = size;
        this.items = new ArrayList<>();
    }

    /**
     * Returns an unmodifiable view of the items in the queue.
     * @return A List of items.
     */
    public List<T> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Returns the current number of items in the queue.
     * @return The number of items.
     */
    public int getCount() {
        return items.size();
    }

    /**
     * Adds an item to the queue.
     * If an item with the same ID already exists, it is replaced with the new item.
     * If the queue is full, the oldest item is removed to make space.
     * @param item The item to add.
     */
    public void enqueue(T item) {
        // Find if an item with the same ID already exists.
        Optional<T> existingItem = items.stream()
                .filter(it -> it.getID().equals(item.getID()))
                .findFirst();

        if (existingItem.isPresent()) {
            T first = existingItem.get();
            items.remove(first);
            items.add(item);
            first.onDisposal(); // Dispose of the old item
        } else {
            if (items.size() < size) {
                items.add(item);
            } else {
                // Queue is full, remove the oldest item
                T first = items.remove(0);
                first.onDisposal();
                items.add(item);
            }
        }
    }

    /**
     * Removes and returns the oldest item from the queue.
     * @return The oldest item.
     * @throws RuntimeException if the queue is empty.
     */
    public T dequeue() {
        if (items.isEmpty()) {
            throw new RuntimeException("queue is empty");
        }
        // remove(0) removes the element at the specified position and returns it.
        return items.remove(0);
    }

    /**
     * Removes a specific item from the queue.
     * @param item The item to remove.
     */
    public void remove(T item) {
        // ArrayList.remove(Object) returns true if the item was found and removed.
        if (items.remove(item)) {
            item.onDisposal();
        }
    }
}
