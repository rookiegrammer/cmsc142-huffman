package huffman;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanNode implements Comparable<HuffmanNode> {
	HuffmanNode left;
	HuffmanNode right;
	
	char[] c;
	int len;
	
	public static HuffmanNode ingest(String text) {
		return ingest(text, 1);
	}
	
	public static HuffmanNode ingest(String text, int ingestSize) {

		int size = ingestSize > 0 ? ingestSize : 1;
		HashMap<String, HuffmanNode> dictionary = new HashMap<String, HuffmanNode>();
		char[] data = text.toCharArray();
		
		String key = "";
		int ingestInd = 0;
		
		// Build ingest frequency table
		for (int i=0; i<data.length; i++) {
			key += data[i];
			if (ingestInd+1 >= size || i >= data.length-1) {
				HuffmanNode node = dictionary.get(key);
				if (node == null) {
					node = new HuffmanNode();
					node.len = 1;
					node.c = key.toCharArray();
					dictionary.put(key, node);
				} else {
					node.len += 1;
				}
				ingestInd = 0;
				key = "";
			} else {
				ingestInd++;
			}
		}
		
		Collection<HuffmanNode> values = dictionary.values();
		HuffmanNode[] nodes = values.toArray(new HuffmanNode[values.size()]);
		
		return buildTreeFromNodes(nodes);
		
	}

	public static HuffmanNode buildTreeFromFrequencyMap(Map<List<Character>, Integer> frequencies) {
		HuffmanNode[] nodes = new HuffmanNode[frequencies.size()];
		int i = 0;
		
		for (List<Character> key: frequencies.keySet()) {
			Integer frequency = frequencies.get(key);
			
			HuffmanNode node = new HuffmanNode();
			node.len = frequency;
			node.c = convertPrimitive(key.toArray(new Character[key.size()]));
			
			nodes[i] = node;
			
			i++;
		}
		
		return buildTreeFromNodes(nodes);
	}
	
	private static char[] convertPrimitive(Character[] arr) {
		char[] out = new char[arr.length];
		for (int i=0; i<arr.length; i++) {
			out[i] = arr[i];
		}
		return out;
	}
	
	private static HuffmanNode buildTreeFromNodes(HuffmanNode[] allNodes) {
		HuffmanNode[] nodes = allNodes;
		
		while (nodes.length > 1) {
			Arrays.sort(nodes);
			
			HuffmanNode[] newNodes = new HuffmanNode[nodes.length-1];
			
			if (nodes.length-2 > 0)
				System.arraycopy(nodes, 2, newNodes, 1, nodes.length-2);
			
			HuffmanNode addNode = new HuffmanNode();
			addNode.left = nodes[0];
			addNode.right = nodes[1];
			addNode.len = addNode.left.len + addNode.right.len;
			
			newNodes[0] = addNode;
			
			nodes = newNodes;
		}
		return nodes[0];
	}
	
	@Override
	public int compareTo(HuffmanNode o) {
		return this.len - o.len;
	}
	
	public String toString() {
		return c == null ? "<"+len+">["+left.toString()+", "+right.toString()+"]" : "'"+(new String(c)) + "' ("+len+")";
	}
}
