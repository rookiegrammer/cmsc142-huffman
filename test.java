import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import huffman.HuffmanCodec;
import huffman.HuffmanNode;

public class test {

	public static void main(String[] args) {
		String data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
		
		HuffmanCodec codec = HuffmanCodec.makeCodec(data, 1);
		System.out.println(codec);
		Boolean[] code = codec.encode(data);
		System.out.println(HuffmanCodec.readBits(code));
		System.out.println(codec.decode(code));
		System.out.println(compressionRatio(data, 1));
		
		System.out.println(compressionRatio(data.toUpperCase().replaceAll("[^A-Z]", ""),
				testsuite.typicalCharacters, testsuite.typicalFrequencies));
	}
	
	public static double compressionRatio(String data, int ingest) {
		HuffmanCodec codec = HuffmanCodec.makeCodec(data, ingest);
		
		return compressionRatio(data, codec);
	}
	
	public static double compressionRatio(String data, char[][] c, int[] freq) {
		int size = c.length;
		if (size > freq.length)
			size = freq.length;
		
		Map<List<Character>, Integer> frequencies = new HashMap<List<Character>, Integer>();
		for (int i=0; i<size; i++) {
			if (c[i].length <= 0) continue;
			
			List<Character> key = new LinkedList<Character>();
			for (char a: c[i]) 
				key.add(a);
			
			frequencies.put(key, freq[i]);
		}

		HuffmanNode tree = HuffmanNode.buildTreeFromFrequencyMap(frequencies);
		HuffmanCodec codec = HuffmanCodec.makeCodec(tree);
		return compressionRatio(data, codec);
	}
	
	public static double compressionRatio(String data, HuffmanCodec codec) {
		final int characterBitSize = 8;
		
		Boolean[] code = codec.encode(data);
		
		return (double) code.length/(data.length()*characterBitSize);
	}
	
	

}
