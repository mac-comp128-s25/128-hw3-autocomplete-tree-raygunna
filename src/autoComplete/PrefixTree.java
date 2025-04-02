package autoComplete;

import java.util.ArrayList;
import java.util.Map;

/**
 * A prefix tree used for autocompletion. The root of the tree just stores links to child nodes (up to 26, one per letter).
 * Each child node represents a letter. A path from a root's child node down to a node where isWord is true represents the sequence
 * of characters in a word.
 */
public class PrefixTree {
    private TreeNode root; 

    // Number of words contained in the tree
    private int size;

    public PrefixTree(){
        root = new TreeNode();
    }

    /**
     * Adds the word to the tree where each letter in sequence is added as a node
     * If the word, is already in the tree, then this has no effect.
     * @param word
     */
    public void add(String word){
        TreeNode currTreeNode = root;
        TreeNode nextTreeNode = new TreeNode();
        for(int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            Map<Character, TreeNode> children = currTreeNode.children;
            if(children.containsKey(letter)) {
                currTreeNode = children.get(letter);
            } else {
                nextTreeNode.letter = letter;
                children.put(letter, nextTreeNode);
                currTreeNode = children.get(letter);
                nextTreeNode = new TreeNode();
            }
            if(i == word.length() - 1 && !currTreeNode.isWord) {
                currTreeNode.isWord = true;
                size++;
            }
        }
    }

    /**
     * Checks whether the word has been added to the tree
     * @param word
     * @return true if contained in the tree.
     */
    public boolean contains(String word){
        TreeNode currTreeNode = root;
        for(int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            Map<Character, TreeNode> children = currTreeNode.children;
            if(children.containsKey(letter)) {
                if (i == word.length() - 1 && children.get(letter).isWord) return true;
                currTreeNode = children.get(letter);
            }
        }
        return false;
    }

    /**
     * Finds the words in the tree that start with prefix (including prefix if it is a word itself).
     * The order of the list can be arbitrary.
     * @param prefix
     * @return list of words with prefix
     */
    public ArrayList<String> getWordsForPrefix(String prefix){
        ArrayList<String> prefixOutput = new ArrayList<>();
        TreeNode currTreeNode = root;
        for(int i = 0; i < prefix.length(); i++) {
            char letter = prefix.charAt(i);
            Map<Character, TreeNode> children = currTreeNode.children;
            if(children.containsKey(letter)) {
                currTreeNode = children.get(letter);
            } else {
                return prefixOutput;
            }
        }
        preOrderTraversalRecursive(currTreeNode, prefixOutput, new StringBuilder(prefix));
        return prefixOutput;
    }

    /*
     * Helper method to recursively call each children of a node and build a string. If that string is a word, add it to the prefix output.
     */
    public void preOrderTraversalRecursive(TreeNode input, ArrayList<String> inputList, StringBuilder sb) {
        if(!input.children.isEmpty()) {
            for(Map.Entry<Character, TreeNode> entry : input.children.entrySet()) {
                sb.append(entry.getKey());
                preOrderTraversalRecursive(entry.getValue(), inputList, sb);
            }
        }
        if(input.isWord) {
            inputList.add(sb.toString());
        }
        sb.deleteCharAt(sb.length() - 1);
    }

    /**
     * @return the number of words in the tree
     */
    public int size(){
        return size;
    }
    
}
