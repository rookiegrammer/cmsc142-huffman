package huffman;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class HuffmanCodec {
	private HashMap<String, Boolean[]> table = new HashMap<String, Boolean[]>();
	private HashMap<List<Boolean>, String> reverseTable = new HashMap<List<Boolean>, String>();
	
	private int ingestSize;
	
	public HuffmanCodec(HuffmanNode node, int ingestSize) {
		this.ingestSize = ingestSize > 0 ? ingestSize : 1;
		processNode(node, new Boolean[] {});
	}
	
	public static HuffmanCodec makeCodec(String data) {
		return makeCodec(HuffmanNode.ingest(data));
	}
	
	public static HuffmanCodec makeCodec(HuffmanNode node) {
		return makeCodec(node, 1);
	}
	
	public static HuffmanCodec makeCodec(String data, int ingestSize) {
		return makeCodec(HuffmanNode.ingest(data, ingestSize), ingestSize);
	}
	
	public static HuffmanCodec makeCodec(HuffmanNode node, int ingestSize) {
		return new HuffmanCodec(node, ingestSize);
	}
	
	
	
	private void processNode(HuffmanNode node, Boolean[] code) {
		if (node.c == null) {
			Boolean[] leftArr = new Boolean[code.length+1];
			Boolean[] rightArr = new Boolean[code.length+1];
			if (code.length > 0) {
				System.arraycopy(code, 0, leftArr, 0, code.length);
				System.arraycopy(code, 0, rightArr, 0, code.length);
			}
			leftArr[code.length] = false;
			rightArr[code.length] = true;
			processNode(node.left, leftArr);
			processNode(node.right, rightArr);
		} else {
			String item = new String(node.c);
			table.put(item, code);
			reverseTable.put(Arrays.asList(code), item);
		}
	}
	
	public static String readBits(Boolean[] arr) {
		String result = "";
		for (Boolean a: arr) {
			result += a ? 1 : 0;
		}
		return result;
	}
	
	public HashMap<String, Boolean[]> getEncodingTable() {
		return new HashMap<String, Boolean[]>(table);
	}
	
	public HashMap<List<Boolean>, String> getDecodingTable() {
		return new HashMap<List<Boolean>, String>(reverseTable);
	}
	
	public String toString() {
		String result = "[ ";
		String[] keys = table.keySet().toArray(new String[0]);
		for (int i=0; i<keys.length; i++) {
			result += "'" + keys[i] + "' -> "+readBits(table.get(keys[i])) + (i < keys.length-1 ? ", " : "");
		}
		return result + " ]";
	}
	
	public class HuffmanEncodingException extends RuntimeException {
		private static final long serialVersionUID = 2L;
		public HuffmanEncodingException() {
			super("Cannot encode unknown key");
		}
	}
	
	public class HuffmanDecodingException extends RuntimeException {
		private static final long serialVersionUID = 3L;
		public HuffmanDecodingException() {
			super("Decoding did not complete");
		}
	}
	
	public Boolean[] encode(String s) throws HuffmanEncodingException {
		LinkedList<Boolean> boolList = new LinkedList<Boolean>();
		
		char[] data = s.toCharArray();
		String key = "";
		int ingestInd = 0;
		for (int i=0; i<data.length; i++) {
			key += data[i];
			if (ingestInd+1 >= ingestSize || i >= data.length-1) {
				Boolean[] code = table.get(key);
				if (code != null) {
					for (Boolean b: code) {
						boolList.add(b);
					}
				} else {
					throw new HuffmanEncodingException();
				}
				ingestInd = 0;
				key = "";
			} else {
				ingestInd++;
			}
		}
		return boolList.toArray(new Boolean[boolList.size()]);
	}
	
	public String decode(Boolean[] code) throws HuffmanDecodingException {
		String text = "";
		List<Boolean> key = new LinkedList<Boolean>();
		for (Boolean bit: code) {
			key.add(bit);
			String decoded = reverseTable.get(key);
			if (decoded != null) {
				key.clear();
				text += decoded;
			}
		}
		if (key.size() > 0) {
			throw new HuffmanDecodingException();
		}
		return text;
	}
	
	
	
}
