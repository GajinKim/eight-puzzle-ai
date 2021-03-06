import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * "A Node is a bookkeeping data structure used to represent the search tree"
 *
 * Node = <state, parent (node), action, depth, path-cost>
 */
public class Node {
    private char[] state; // representation of a physical configuration (i.e. 8puzzle grid)
    private ArrayList<Node> children;
    private Node parent;
    private Action action;
    private int depth;
    private int cost; // g(n)
    private int totalCost; // f(n) = g(n) + h(n)
    private boolean visited;


    /**
     * Constructor
     *
     * @param f provided file
     */
    public Node(File f) {
        state = new char[9];
        try {
            Scanner fScan = new Scanner(f);
            for (int i = 0; i < state.length; i++) {
                state[i] = fScan.next().charAt(0);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        children = new ArrayList<Node>();
        parent = null;
        depth = 0;
    }

    public Node(Node n) {
        this.state = n.state.clone();
        this.children = new ArrayList<Node>();
    }

    public Node(char[] state) {
        this.state = state.clone();
        children = new ArrayList<>();
    }

    /**
     * Getters
     */
    public char[] getState() {
        return state;
    }
    public ArrayList<Node> getChildren() { return children; }
    public Node getParent() {
        return parent;
    }
    public Action getAction() { return action; }
    public int getDepth() { return depth; }
    public int getCost() { return cost; }
    public int getTotalCost() { return totalCost; }
    public boolean getVisited() { return visited; }

    /**
     * Setters
     */
    public void setState(char[] state) {
        this.state = state;
    }
    public void setChildren(ArrayList<Node> children) { this.children = children; }
    public void setParent(Node parent) {
        this.parent = parent;
    }
    public void setAction(Action action) { this.action = action; }
    public void setDepth(int depth) { this.depth = depth; }
    public void setCost(int cost) { this.cost = cost; }
    public void setTotalCost(int totalCost) { this.totalCost = totalCost; }
    public void setVisited(boolean visited) {this.visited = visited; }

    /**
     * Add child
     */
    public void addChild(Node child) { children.add(child); }

    /**
     * Actions
     */
    public void moveLeft() {
        int gapPos = getGapPosition();
        int x = getXCoord(gapPos);
        int y = getYCoord(gapPos);
        if (x != 2) {
            setValue(x, y, getValueAt(x + 1, y));
            setValue(x + 1, y, '_');
        }
        action = Action.L;
    }
    public void moveRight() {
        int gapPos = getGapPosition();
        int x = getXCoord(gapPos);
        int y = getYCoord(gapPos);
        if (x != 0) {
            setValue(x, y, getValueAt(x - 1, y));
            setValue(x - 1, y, '_');
        }
        action = Action.R;
    }
    public void moveUp() {
        int gapPos = getGapPosition();
        int x = getXCoord(gapPos);
        int y = getYCoord(gapPos);
        if (y != 2) {
            setValue(x, y, getValueAt(x, y + 1));
            setValue(x, y + 1, '_');
        }
        action = Action.U;
    }
    public void moveDown() {
        int gapPos = getGapPosition();
        int x = getXCoord(gapPos);
        int y = getYCoord(gapPos);
        if (y != 0) {
            setValue(x, y, getValueAt(x, y - 1));
            setValue(x, y - 1, '_');
        }
        action = Action.D;
    }

    /**
     * String representation of given puzzle
     */
    public String toString() {
        return "+-------+" + "\n" +
                "| " + state[0] + " " + state[1] + " " + state[2] + " |" + "\n" +
                "| " + state[3] + " " + state[4] + " " + state[5] + " |" + "\n" +
                "| " + state[6] + " " + state[7] + " " + state[8] + " |" + "\n" +
                "+-------+";
    }

    /**
     * Determines if given puzzle is solvable based on number of inversions
     *
     * @return True if solvable; false otherwise
     */
    public Boolean isSolvable() {
        return numInversions() % 2 == 0; // solvable if number of inversions is even
    }

    /**
     * Number of inversions in board
     *
     * @return number of inversions
     */
    public int numInversions() {
        int inversions = 0;

        for (int i = 0; i < state.length; i++) {
            for (int j = i + 1; j < state.length; j++) {
                if (state[i] != '_' && state[j] != '_') {
                    if (Character.valueOf(state[i]) > Character.valueOf(state[j])) {
                        inversions++;
//                        System.out.println("(" + state[i] + "," + state[j] + ")"); // for testing
                    }
                }
            }
        }

        return inversions;
    }

    /**
     * Referenced from AIMA:
     * https://github.com/aimacode/aima-java/blob/AIMA3e/aima-core/src/main/java/aima/core/environment/eightpuzzle/EightPuzzleBoard.java
     */
    private int getXCoord(int pos) {
        return pos % 3;
    }
    private int getYCoord(int pos) {
        return pos / 3;
    }
    private int getPosition(int x, int y) {
        return x + 3 * y;
    }
    private char getValueAt(int x, int y) {
        return state[getPosition(x, y)];
    }
    public int getGapPosition() {
        return getPositionOf('_');
    }
    public int getPositionOf(char val) {
        for (int i = 0; i < 9; i++)
            if (state[i] == val)
                return i;
        return -1;
    }
    private void setValue(int x, int y, char val) {
        int pos = getPosition(x, y);
        state[pos] = val;
    }
}
