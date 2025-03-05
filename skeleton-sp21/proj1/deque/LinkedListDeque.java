package deque;


import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T> {
    private Node sentinel;
    private int size;

    private class Node{
         T item;
         Node next;
         Node prev;

        public Node(T item, Node next, Node prev){
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;  // 初始时，next 和 prev 都指向自己
        sentinel.prev = sentinel;
        size = 0;
    }

    public void addFirst(T item){
        Node newNode = new Node(item, sentinel.next, sentinel);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size += 1;
    }

    public void addLast(T item){
        Node newNode = new Node(item, sentinel, sentinel.prev);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size += 1;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        Node current = sentinel.next;
        while(current!=sentinel){
            System.out.print(current.item+" ");
            current = current.next;
        }
        System.out.println("The end of the deque.");
    }

    public T removeFirst(){
        if(isEmpty()){return null;}
        Node current  = sentinel.next;
        current.next.prev = sentinel;
        sentinel.next = current.next;
        size -= 1;
        return current.item;
    }

    public T removeLast(){
        if (isEmpty()) {
            return null;  // ✅ 先检查空队列
        }
        Node lastNode = sentinel.prev;
        sentinel.prev = lastNode.prev;
        lastNode.prev.next = sentinel;
        size -= 1;
        return lastNode.item;
    }

    public T get(int index){
        if(index >= size || index < 0){throw new IndexOutOfBoundsException();}
        Node current = sentinel.next;
        for(int i = 0; i < index; i++){
            current = current.next;
        }
        return current.item;
    }

    public T getRecursive(int index){
        if(index >= size || index < 0){throw new IndexOutOfBoundsException();}
        return getRecursiveHelper(sentinel.next, index);
    }

    public T getRecursiveHelper(Node current, int index){
        if(index==0) return current.item;
        return getRecursiveHelper(current.next, index-1);
    }

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

    public Iterator<T> iterator(){
        return new Iterator<T>() {
            private Node curr = sentinel.next;

            @Override
            public boolean hasNext() {
                return curr != null;
            }

            @Override
            public T next() {
                T item = curr.item;
                curr = curr.next;
                return item;
            }
        };
    }
}
