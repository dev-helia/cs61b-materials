package deque;
import java.util.Comparator;


public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c){
        super();  // 调用父类的构造方法
        this.comparator = c;
    }
    public T max(){
        if(isEmpty()) return null;
        T maxItem = get(0);
        for(int i=1; i<size(); i++){
            T currentItem = get(i);
            if(comparator.compare(maxItem, currentItem) > 0) maxItem = currentItem;
        }
        return maxItem;
    }
    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }

        T maxItem = get(0);
        for (int i = 1; i < size(); i++) {
            T currentItem = get(i);
            if (c.compare(currentItem, maxItem) > 0) {
                maxItem = currentItem;
            }
        }
        return maxItem;
    }

}