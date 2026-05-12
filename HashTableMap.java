import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class HashTableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {
    ///instance variables
    private int size;
    private int capacity;
    protected LinkedList<Pair>[] table = null;

    protected class Pair {
        public KeyType key;
        public ValueType value;
            public Pair(KeyType key, ValueType value) {
                this.key = key;
                this.value = value;
            }
    }

    /// constructor
    public HashTableMap(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException();
        }

        this.table = (LinkedList<Pair>[]) new LinkedList[capacity];
        this.size = 0;
        this.capacity = capacity;
    }

    public HashTableMap(){
        this(8);
    }

    /**
     * Adds a new key,value pair/mapping to this collection.
     * @param key the key of the key,value pair
     * @param value the value that key maps to (may be null)
     * @throws IllegalArgumentException if key already maps to a value without
     *         making any changes to the table
     * @throws NullPointerException if key is null
     */
    @Override
    public void put(KeyType key, ValueType value) throws IllegalArgumentException {
        if (key == null) {
            throw new NullPointerException("Key value cannot be null");
        }

        int index = Math.abs(key.hashCode()) % capacity;

        if (table[index] == null){
            table[index] = new LinkedList<>();
        }

        for (Pair p : table[index]) {
        if (p.key.equals(key)) {
            throw new IllegalArgumentException("Key already exists in the table");
        }
        }

        table[index].add(new Pair(key,value));
        //increment size
        size++;

        //if size / capacityy >= .75, resize
        if ((double) size/capacity >= 0.75) {
            resize();
        }

    }

    //helper method that resizes in put method
    private void resize() {
        capacity = capacity * 2;
        LinkedList<Pair>[] oldTable = table;
        table = (LinkedList<Pair>[]) new LinkedList[capacity];
        size = 0;

        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] != null) {
                for (Pair p : oldTable[i]) {
                put(p.key, p.value);
                }
            }
        }
    }

    /**
     * Checks whether a key maps to a value in this collection.
     * @param key the key to check
     * @throws NullPointerException if key is null
     * @return true if the key maps to a value, and false is the key doesn't
     *         map to a value
     */
    @Override
    public boolean containsKey(KeyType key) {
        if (key == null){
            throw new NullPointerException("key cannot be null");
        }

        int index = Math.abs(key.hashCode()) % capacity;

        ///if there is no pair in key's index calculated from hash code
        if (table[index] == null) {
            return false;
        }

        //loop thru all pairs in the table
        //if key at calculated index equals key we are looking for, then return true
        for (Pair p : table[index]) {
            if (p.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the specific value that a key maps to.
     * @param key the key to look up
     * @return the value that key maps to
     * @throws NoSuchElementException when key is not stored in this collection
     * @throws NullPointerException if key is null
     */
    @Override
    public ValueType get(KeyType key) throws NoSuchElementException {
        if (key == null){
            throw new NullPointerException("key cannot be null");
        }

        int index = Math.abs(key.hashCode()) % capacity;
        if (containsKey(key) == true) {
            //loop thru all pairs in the table
            //if key at calculated location is equal to seacrhed for, return its value
            for (Pair p : table[index]) {
                if (p.key.equals(key)) {
                    return p.value;
                }
            }

        }
        throw new NoSuchElementException("There is no such key within the collection");
    }

    /**
     * Remove the mapping for a key from this collection.
     * @param key the key whose mapping to remove
     * @return the value that the removed key mapped to
     * @throws NoSuchElementException when key is not stored in this collection
     * @throws NullPointerException if key is null
     */
    @Override
    public ValueType remove(KeyType key) throws NoSuchElementException {
        if (key == null){
            throw new NullPointerException("key cannot be null");
        }

        int index = Math.abs(key.hashCode()) % capacity;
        if (containsKey(key) == true) {
            //loop thru all pairs in the table
            //if key at calculated location is equal to seacrhed for, return its value
            for (Pair p : table[index]) {
                if (p.key.equals(key)) {
                    table[index].remove(p);
                    size--;
                    return p.value;
                }
            }
        }
        throw new NoSuchElementException("There is no such key within the collection");
    }


    /**
     * Removes all key,value pairs from this collection without changing the
     * capacity of the underlying array.
     */
    @Override
    public void clear() {
        for (int i = 0; i < table.length; i ++){
            table[i] = null;
        }
        size = 0;
    }

    /**
     * Retrieves the number of keys stored in this collection.
     * @return the number of keys stored in this collection
     */
    @Override
    public int getSize() {
        return this.size;
    }

    /**
     * Retrieves this collection's capacity.
     * @return the size of the underlying array for this collection
     */
    @Override
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Retrieves this collection's keys.
     * @return a list of keys in the underlying array for this collection
     */
    @Override
    public List<KeyType> getKeys() {
        List<KeyType> keys = new LinkedList<KeyType>();
        for (int i = 0; i < table.length; i++){
            if (table[i] != null) {
                for (Pair p : table[i]) {
                    keys.add(p.key);
                }
            }
        }
        return keys;

    }

    /**
     * Tests for resizing
     * Triggers the resize and uses getCapacity and put to test that resizing has been done
     */
    @Test
    public void testResizeTrigger() {
        HashTableMap hash = new HashTableMap(10);
        hash.put("mahika", 14);
        hash.put("pranav", 67);
        hash.put("manita", 16);
        hash.put("blue", 9);
        hash.put("teal", 4);
        hash.put("aqua", 90);
        hash.put("movie", 19);

        assertEquals(10, hash.getCapacity());

        hash.put("theater", 11);

        //after the 8th element is inserted, load factor gets higher than 75%
        assertEquals(20, hash.getCapacity()); //capacity should be doubled to resizing
    }

    /**
     * Tests for rehashing
     * after resizing is triggered, checks that rehashing has correctly kept keys
     * uses getCapacity, put and get methods as well
     */
    @Test
    public void testResizeRehashing() {
        HashTableMap hash = new HashTableMap(4);
        hash.put("first", 30);
        hash.put("second", 17);
        assertEquals(4, hash.getCapacity()); // check that resizing has not yet happened
        hash.put("third", 88); //resizing occurs in this step, same for rehashing


        assertEquals(8, hash.getCapacity()); // check that resizing has happened

        ///check that even after rehashing, values are still contained in the table
        /// exception would be thrown if rehashing was done incorrectly
        assertEquals(30, hash.get("first"));
        assertEquals(17, hash.get("second"));
        assertEquals(88, hash.get("third"));

    }

    /**
     * Tests that contains key method is working correctly
     */
    @Test
    public void testContainsKeys() {
        HashTableMap hash = new HashTableMap(4);
        hash.put("mwahahahah", 90);
        hash.put("hehehehehe", 72);

        assertTrue(hash.containsKey("hehehehehe")); //check that table has this
        assertTrue(!hash.containsKey("gagagaga")); // make sure table does not have this undefined key

    }

    /**
     * Tests that remove method is working correctly
     * tests that correct keys are removed and others stay
     */
    @Test
    public void testRemove() {
        HashTableMap hash = new HashTableMap(4);
        hash.put("hello world", 90);
        hash.put("goodbye", 72);

        hash.remove("goodbye"); //remove this key

        assertTrue(!hash.containsKey("goodbye")); // this key should no longer be contained in the table
        assertTrue(hash.containsKey("hello world")); //this key should still exist
    }

    /**
     * Tests clear, getSize, and getKeys method
     */
    @Test
    public void testClearAndGetSizeCapacityAndKey() {
        HashTableMap hash = new HashTableMap(4);
        hash.put("hello world", 90);
        hash.put("goodbye", 72);

        assertEquals(2, hash.getSize()); //check for size
        assertTrue(hash.getKeys().contains("hello world"));
        assertTrue(hash.getKeys().contains("goodbye"));

        hash.clear(); //clear

        assertEquals(0, hash.getSize()); //check that size is now 0

        assertTrue(hash.getKeys().isEmpty()); //check if getKeys outputs an empty list as it should

    }

}//end of HashTableMap