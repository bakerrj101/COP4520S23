import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PresentLinkedList {
    private Node head;

    public PresentLinkedList() {
        this.head = new Node(-1);
    }
    private boolean validate (Node prevNode, Node curNode) {
        return !prevNode.marked && !curNode.marked && prevNode.next == curNode;
    }

    public boolean add(int i) {
        while (true) {
            Node prevNode = head;
            Node curNode = head.next;
            while (curNode != null && curNode.val < i) {
                prevNode = curNode;
                curNode = curNode.next;
            }
            prevNode.lock();
            try {
                if (curNode != null) {
                    curNode.lock();
                    try {
                        if (validate(prevNode, curNode)) {
                            if (curNode.val == i) {
                                return false;
                            } else {
                                prevNode.next = new Node(i, curNode);
                                return true;
                            }
                        }
                    } finally {
                        curNode.unlock();
                    }
                } else {
                    if (!prevNode.marked) {
                        prevNode.next = new Node(i, null);
                        return true;
                    }
                }
            } finally {
                prevNode.unlock();
            }
        }
    }

    public boolean remove() {
        while (true) {
            Node prevNode = head;
            Node curNode = head.next;
            prevNode.lock();
            try {
                if (curNode == null) {
                    return false;
                }
                curNode.lock();
                try {
                    if (validate(prevNode, curNode)) {
                        curNode.marked = true;
                        prevNode.next = curNode.next;
                        return true;
                    }
                } finally {
                    curNode.unlock();
                }
            } finally {
                prevNode.unlock();
            }
        }
    }

    public boolean contains(int i) {
        Node curNode = head;
        while (curNode != null && curNode.val < i) {
            curNode = curNode.next;
        }
        return curNode != null && curNode.val == i && !curNode.marked;
    }

    public boolean isEmpty() {
        return head.next == null;
    }
}

class Node {
    int val;
    boolean marked;
    Node next;
    Lock lock;

    public Node (int val)  {
        this.val = val;
        this.next = null;
        marked = false;
        lock = new ReentrantLock();
    }
    public Node (int val, Node next)  {
        this.val = val;
        this.next = next;
        marked = false;
        lock = new ReentrantLock();
    }
    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }
}