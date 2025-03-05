package deque;


import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int first = 0;
    private int last = 0;
    private int size = 0;
    private static final int INITIAL_CAPACITY = 8;

    public ArrayDeque() {
        items = (T[]) new Object[INITIAL_CAPACITY];
        first = 0;
        last = 0;
        size = 0;
    }
    public void addFirst(T item){

        first = (first - 1 + items.length) % items.length;
        items[first] = item;
        size += 1;

    }  // 在队列头部添加元素
    public void addLast(T item){
        items[last] = item;
        last = (last + 1) % items.length;
        size += 1;
    }   // 在队列尾部添加元素
    public boolean isEmpty(){
        return size == 0;
    }      // 判断队列是否为空（但要用 Deque 接口的默认实现）
    public int size(){
        return size;
    }             // 返回队列中的元素个数
    public void printDeque(){
        int index = (first + 1) % items.length;
        for(int i = 0; i < size; i++){
            System.out.println(items[index] + " ");
            index = (index + 1) % items.length;
        }
        System.out.println();
    }      // 依次打印队列中的元素

    public T removeFirst() {
        if (size == 0) { // 队列为空
            return null;
        }

        first = (first + 1) % items.length;  // 🟢 first 往后移动一位，指向新的头部
        T item = items[first];  // 取出被删除的元素
        items[first] = null;  // 避免内存泄漏
        size--;  // 更新 size

        return item;  // 返回被删除的元素
    }  // 删除并返回队列头部的元素

    public T removeLast(){
        if(size == 0){
            return null;
        }
        last = (last - 1 + items.length) % items.length;
        T item = items[last];
        items[last] = null;
        size--;
        return item;
    }         // 删除并返回队列尾部的元素

    public T get(int index){
        if(index < 0 || index >= size){throw new IndexOutOfBoundsException();}
        return items[index];
    }
        // 根据索引获取元素（不能改变队列状态）

    public Iterator<T> iterator(){
        return new Iterator<T>() {
            private int pos = first;
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count < size;
            }

            @Override
            public T next() {
                if(!hasNext()){throw new NoSuchElementException();}
                T item = items[pos];
                pos = (pos + 1) % items.length;
                count++;
                return item;
            }
        };
    } // 让 Deque 变成 Iterable
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if(!(o instanceof Deque<?>)) return false;
        Deque<?> other = (Deque<?>) o;
        if (this.size != other.size()) return false;
        Iterator<T> thisIter = this.iterator();
        Iterator<?> otherIter = other.iterator();

        while(thisIter.hasNext()){
            T thisItem = thisIter.next();
            Object otherItem = otherIter.next();
            if(!thisItem.equals(otherItem)){
                return false;
            }
        }

        return true;
    }
}
