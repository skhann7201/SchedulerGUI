package com.example.project3;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The List class manages a dynamic array of Appointment objects,
 * providing methods to add, remove, search, and sort appointments
 * by various criteria, while resizing the array as needed.
 *
 * @author Shahnaz Khan
 */
public class List<E> implements Iterable<E> {

    private E[] objects;
    private int size;
    private static final int INITIAL_CAPACITY = 4;

    /**
     * Constructor that initializes the appointments array to size 4 and size to 0.
     */
    public List() {
        this.objects = (E[]) new Object[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * This helper method grows the size of the array by 4.
     */
    private void grow() {
        E[] newArray = (E[])new Object[objects.length + INITIAL_CAPACITY];
        for(int i =0; i<size;i++){
            newArray[i] = objects[i];
        }
        objects = newArray;
    }

    /**
     * This helper method finds a specific object in the array.
     * @param e the objects to find
     * @return index of the object if found, -1 otherwise
     */
    public boolean contains(E e) {
        for (int i = 0; i < size; i++) {
            if (objects[i] != null && objects[i].equals(e)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Adds the specified appointment to the object array,
     * growing it in size if the array is already full.
     * @param e the object to add
     */
    public void add(E e) {
        if (size == objects.length) {
            grow();
        }
        objects[size] = e;
        size++;
    }

    /**
     * Removes the specified object from the objects array,
     * shifting every element after the removed element to the left by 1.
     * @param e the object to be removed
     */
    public void remove(E e) {
        int index = indexOf(e);
        if (index != -1) {
            for (int i = index; i < size - 1; i++) {
                objects[i] = objects[i + 1];
            }
            objects[--size] = null;
        }
    }

    /**
     * Checks if objects array is empty
     * @return true if empty, false otherwise
     */
    public boolean isEmpty(){
        return size==0;
    }

    /**
     * Getter method that returns the total amount of objects in the array.
     * @return int, total amount of appointments
     */
    public int size() {
        return size;
    }

    /**
     *
     * @return
     */
    @Override
    public Iterator<E> iterator(){
        return new ListIterator();
    }

    /**
     * Returns the object at the specified index.
     *
     * @param index the index of the object to retrieve
     * @return the object at the specified index
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public E get(int index) {
        if (index >= 0 && index < size) {
            return objects[index];
        } else {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
    }

    /**
     * Sets the Object e, at index
     * @param index, index to set object at
     * @param e, the object to be se in index
     */
    public void set(int index, E e) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        objects[index] = e;
    }

    /**
     * Checks which index object e is at
     * @param e, finding the index of this object
     * @return the index of e, -1 otherwise
     */
    public int indexOf(E e){
        for(int i=0;i< size;i++){
            if(objects[i].equals(e)){
                return i;
            }
        }
        return -1;
    }

    /**
     * A private inner class that implements the Iterator interface for iterating
     * over elements of the outer list class. It maintains the current index and
     * provides methods to check if there are more elements and to return the next element.
     * @param <E> the type of elements returned by this iterator
     */
    private class ListIterator<E> implements Iterator<E>{
        private int currentIndex =0;
        @Override
        public boolean hasNext(){
            return currentIndex < size;
        }

        @Override
        public E next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            return (E) objects[currentIndex++];
        }
    }

}

