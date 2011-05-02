package lt.ltech.numbers.player;

import java.util.ArrayList;
import java.util.List;

public class Pool<T extends Comparable<T>> implements Comparable<Pool<T>> {
    private List<T> pool;
    private int num;

    public Pool(List<T> pool, int num) {
        this.pool = pool;
        this.num = num;
    }

    public boolean contains(Pool<T> pool) {
        return this.pool.containsAll(pool.pool);
    }

    public List<T> getPool() {
        return pool;
    }

    public void setPool(ArrayList<T> pool) {
        this.pool = pool;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (T i: pool) {
            sb.append(i);
        }
        return sb.append("/").append(num).toString();
    }

    public int hashCode() {
        return num * pool.size();
    }

    public boolean equals(Object pool) {
        if (pool == null || !(pool instanceof Pool)) {
            return false;
        }
        Pool<?> p = (Pool<?>) pool;
        return p.num == num && p.pool.equals(this.pool);
    }

    @Override
    public int compareTo(Pool<T> o) {
        int c = o.pool.size() - pool.size();
        if (c == 0) {
            c = o.num - num;
        }
        if (c == 0) {
            for (int i = 0; i < o.pool.size(); i++) {
                c = o.pool.get(i).compareTo(pool.get(i));
                if (c != 0) {
                    break;
                }
            }
        }
        return c;
    }
}
