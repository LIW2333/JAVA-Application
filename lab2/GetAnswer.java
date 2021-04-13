
import java.util.*;

public class GetAnswer {
    /*     _ _ _ _ _ 
	 *    |         |
	 *    |    1    |   
	 *    | 6     2 | 
	 *    |    7    |
	 *    |_ _ _ _ _|        
	 *    |         |
	 *    |         |   
	 *    | 5     3 | 
	 *    |    4    |
	 *    |_ _ _ _ _| 
	 *    
	 *   火柴的位置对应的字符串位置
     *      
     *   字符串的格式：{标志位：0为数字，1为运算符， 1-7火柴位置}
     *  
	 * 
	 */
    String[] numbers = { 
        "01111110",// 0
        "00110000",// 1
        "01101101",// 2
        "01111001",// 3
        "00110011",// 4
        "01011011",// 5
        "01011111",// 6
        "01110000",// 7
        "01111111",// 8
        "01111011",// 9
    };
    String[] operator = { 
            "11000000",// +
            "10000000",// -
            "11100000",// =
    };
    String[] operatorChar = { "+",// +
            "-",// -
            "=",// =
    };
    //存储返回结果的结构
    ArrayList<String> ans_list = new ArrayList<>();
    //判断8为二进制字符串是否表示数字
	public boolean isDigit(String str) {
		
		if (str.startsWith("0")) {
			for (int i = 0; i < numbers.length; i++) {
				if (str.equals(numbers[i])) {
					return true;
				}
			}
		}
		return false;
	}
	//判断8为二进制字符串是否表示运算符号
	public boolean isOperator(String str) {
		return str.startsWith("1");
    }
    public int bin2digit(String str){
        int result = 0;
        int len = str.length();
        int cnt = len/8;
        String num = "";
        for(int j = 0; j<cnt;++j){
            num = str.substring(j*8, (j+1)*8);
            if(isDigit(num) == false){
                return -1;
            }
            result = result*10;
            for(int i=0;i<10;++i){
                if(numbers[i].equals(num)){
                    result += i;
                }
            }
        }
        return result;
    } 
    public String bin2ope(String str){
        if(isOperator(str) == false){
            return "";
        }
        for(int i=0;i<3;++i){
            if(operator[i].equals(str)){
                return operatorChar[i];
            }
        }
        return "";
    }
    public String decode(String str){
        String result = "";
		int length = str.length();
		int cnt = length / 8;
		String digit = "";
		String operator = "";
		//生成计算表达式
		for (int i = 0; i < cnt; i++) {
            String section = str.substring(i * 8, (i + 1) * 8);
            if(!isDigit(section) && !isOperator(section)){
                return "";
            }
			if (isDigit(section)) {
				digit = "" + bin2digit(section);
				//把操作数加入队列
				
				result+=digit;
				digit = "";
 
			} else {
				if (digit.length() > 0 && digit.startsWith("0")) {
					return "";
				}
                operator = bin2ope(section).toString();
                result += operator;
				operator = "";
			}
		}
		result += digit;
		return result;
    }
    public boolean isLegal(String str){
        // GetAnswer ga = new GetAnswer();
        str = decode(str);
        if(str.isEmpty()){
            return false;
        }
        int result = 0;
        int last_sig = 1;   //0：数字， 1：运算符
        String tmpope = "";
        String tmpnum = "";
        int len = str.length();
        int i=0;
        for(i=0;i<len;++i){
            if(str.charAt(i) == '='){
                if(tmpope.isEmpty()|| tmpope.equals("+")){
                    result += Integer.parseInt(tmpnum);
                }else{
                    result -= Integer.parseInt(tmpnum);
                }
                break;
            }else if(str.charAt(i) == '+'){
                if(tmpope.isEmpty()|| tmpope.equals("+")){
                    result += Integer.parseInt(tmpnum);
                }else{
                    result -= Integer.parseInt(tmpnum);
                }
                tmpope = "+";
                last_sig = 1;
            }else if(str.charAt(i)== '-'){
                if(tmpope.isEmpty()|| tmpope.equals("+")){
                    result += Integer.parseInt(tmpnum);
                }else{
                    result -= Integer.parseInt(tmpnum);
                }
                tmpope = "-";
                last_sig = 1;
            }else{
                if(last_sig == 1){
                    tmpnum = str.charAt(i) + "";
                    last_sig =0;
                }else{
                    tmpnum += str.charAt(i);
                }
            }
        }
        i++;
        tmpnum = str.substring(i, len);
        int exp_res = Integer.parseInt(tmpnum);
        if(exp_res == result){
            return true;
        }
        return false;
    }
    
    public String parser(String experession){
        String binary_exp="";
        for(int i=0;i<experession.length();++i){
            if(experession.charAt(i) == '-'){
                binary_exp += "10000000";
            }else if(experession.charAt(i) == '+'){
                binary_exp += "11000000";
            }else if(experession.charAt(i) == '='){
                binary_exp += "11100000";
            }else{
                char num_char = 0;
                for(int j=0;j<10;++j){
                    num_char = (char) (j + 48);
                    if(experession.charAt(i) == num_char){
                        binary_exp += numbers[j];
                    }
                }
            }
        }
        return binary_exp;
    }
    public boolean isMoveable(String s, int index) {
		String temp = s.intern();
		char c = temp.charAt(index);
		if (c == '1') {
            return true;
		}
		return false;
	}
   //判断某个数字的位置上受否可以接受一根火柴
	public boolean isAddable(String s, int index) {
		String temp = s.intern();
		char c = temp.charAt(index);
		if (c == '0') {
			return true;
		}
		return false;
    }
    public boolean isEmpty(String str, int index){
        if(str.charAt(index) == '0'){
            return true;
        }else{
            return false;
        }
    }
    public void getMoveAnswer(String str, int mv_num){
        // ArrayList<String> ans_list = new ArrayList<>();
        int cnt = str.length() /8;
        // int cnt_mv = 0;
        String res_str = str.intern();
        String last_str = res_str;
        // GetAnswer ga = new GetAnswer();
        if(mv_num == 0){
            if(isLegal(str)) {
                str = decode(str);
                ans_list.add(str);
            }
        }else{
            for(int i=0;i<cnt;++i){
                String section = str.substring(i*8, (i+1)*8);
                if(isOperator(section)){
                    if(section.equals("11000000")){
                        for(int k=0;k<cnt;++k){
                            String tar = str.substring(i*8, (i+1)*8);
                            if(isOperator(tar)){
                                if(k!=i){
                                    if(tar.equals("100000000")){
                                        last_str = res_str;
                                        int start = i * 8+1;
                                        int dist = k * 8+1;
                                        //生成新的字符串
                                        res_str = res_str.substring(0, start) + "0" + res_str.substring(start+1);
                                        res_str = res_str.substring(0, dist) + "1" + res_str.substring(dist+1);
                                        getMoveAnswer(res_str, mv_num-1);
                                        // cnt_mv++;
                                        // if(cnt_mv == mv_num){
                                        //     if(isLegal(res_str)) ans_list.add(res_str);
                                        // }
                                        // cnt_mv--;
                                        res_str = last_str; 
                                    }
                                }
                            }else{
                                for(int l=1; l<8;++l){
                                    if(isEmpty(tar,l)){
                                        last_str = res_str;
                                        int start = i * 8+1;
                                        int dist = k * 8+l;
                                        //生成新的字符串
                                        res_str = res_str.substring(0, start) + "0" + res_str.substring(start+1);//移动'+'字符
                                        res_str = res_str.substring(0, dist) + "1" + res_str.substring(dist+1);
                                        // cnt_mv++;
                                        // if(cnt_mv == mv_num){
                                        //     if(isLegal(res_str)) ans_list.add(res_str);
                                        // }
                                        // cnt_mv--;
                                        
                                        getMoveAnswer(res_str, mv_num-1);
                                        res_str = last_str;
                                    }
                                }
                            }
                        }
                    }
                }else{
                    for(int j=1;j<8;++j){
                        if(!isEmpty(section,j)){
                            for(int k=0;k<cnt;++k){
                                String tar = str.substring(k*8, (k+1)*8);
                                if(isOperator(tar)){
                                    if(tar.equals("100000000")){
                                        last_str = res_str;
                                        int start = i * 8+j;
                                        int dist = k * 8+1;
                                        //生成新的字符串
                                        res_str = res_str.substring(0, start) + "0" + res_str.substring(start+1);
                                        res_str = res_str.substring(0, dist) + "1" + res_str.substring(dist+1);
                                        // cnt_mv++;
                                        // if(cnt_mv == mv_num){
                                        //     if(isLegal(res_str)) ans_list.add(res_str);
                                        // }
                                        // cnt_mv--;
                                        
                                        getMoveAnswer(res_str, mv_num-1);
                                        res_str = last_str; 
                                    }
                                }else{
                                    for(int l=1; l<8;++l){
                                        if(isEmpty(tar,l)){
                                            last_str = res_str;
                                            int start = i * 8+j;
                                            int dist = k * 8+l;
                                            //生成新的字符串
                                            res_str = res_str.substring(0, start) + "0" + res_str.substring(start+1);//移动'+'字符
                                            res_str = res_str.substring(0, dist) + "1" + res_str.substring(dist+1);
                                            // cnt_mv++;
                                            // if(cnt_mv == mv_num){
                                            //     if(isLegal(res_str)) ans_list.add(res_str);
                                            // }
                                            // cnt_mv--;
                                            
                                            getMoveAnswer(res_str, mv_num-1);
                                            res_str = last_str;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public void getRemoveAnswer(String str, int mv_num){
        // ArrayList<String> ans_list = new ArrayList<>();
        int cnt = str.length() /8;
        // int cnt_mv = 0;
        String res_str = str.intern();
        String last_str = res_str;
        // GetAnswer ga = new GetAnswer();
        if(mv_num == 0){
            if(isLegal(str)) {
                str = decode(str);
                ans_list.add(str);
            }
        }else{
            for(int i=0;i<cnt;++i){
                String section = str.substring(i*8, (i+1)*8);
                if(isOperator(section)){
                    if(section.equals("11000000")){
                        last_str = res_str;
                        int index = i * 8+1;
                        //生成新的字符串
                        res_str = res_str.substring(0, index) + "0" + res_str.substring(index+1);
                        getRemoveAnswer(res_str, mv_num - 1);
                        // cnt_mv++;
                        // if(cnt_mv == mv_num){
                        //     if(isLegal(res_str)) ans_list.add(res_str);
                        // }
                        // cnt_mv--;
                        res_str = last_str; 
                    }
                }else{
                    for(int j=1;j<8;++j){
                        if(isMoveable(section, j)){
                            last_str = res_str;
                            int index = i * 8+j;
                            //生成新的字符串
                            res_str = res_str.substring(0, index) + "0" + res_str.substring(index+1);
                            getRemoveAnswer(res_str, mv_num-1);
                            // cnt_mv++;
                            // if(cnt_mv == mv_num){
                            //     if(isLegal(res_str)) ans_list.add(res_str);
                            // }
                            // cnt_mv--;
                            res_str = last_str;
                        }
                    }
                }
            }
        }
    }
    public void getAddAnswer(String str, int mv_num){
        // ArrayList<String> ans_list = new ArrayList<>();
        int cnt = str.length() /8;
        // int cnt_mv = 0;
        String res_str = str.intern();
        String last_str = res_str;
        if(mv_num == 0){
            if(isLegal(str)) {
                str = decode(str);
                ans_list.add(str);
            }
        }else{
            for(int i=0;i<cnt;++i){
                String section = str.substring(i*8, (i+1)*8);
                if(isOperator(section)){
                    if(section.equals("10000000")){
                        last_str = res_str;
                        int index = i * 8+1;
                        //生成新的字符串
                        res_str = res_str.substring(0, index) + "1" + res_str.substring(index+1);
                        // cnt_mv++;
                        // if(cnt_mv == mv_num){
                        //     if(isLegal(res_str)) ans_list.add(res_str);
                        // }
                        // cnt_mv--;
                        
                        getAddAnswer(res_str, mv_num -1);
                        res_str = last_str; 
                    }
                }else{
                    for(int j=1;j<8;++j){
                        if(isAddable(section, j)){
                            last_str = res_str;
                            int index = i * 8+j;
                            //生成新的字符串
                            res_str = res_str.substring(0, index) + "1" + res_str.substring(index+1);
                            // cnt_mv++;
                            // if(cnt_mv == mv_num){
                            //     if(isLegal(res_str)) ans_list.add(res_str);
                            // }
                            // cnt_mv--;
                            
                            getAddAnswer(res_str, mv_num-1);
                            res_str = last_str; 
                        }
                    }
                }
            }
        }

        // return ans_list;
    }
    public static ArrayList<String> getAnswer(String str, int mode, int mv_num){

        // ArrayList<String> ans_list = new ArrayList<>();
        GetAnswer ga = new GetAnswer();
        str = ga.parser(str);
        if(mode == 3){
            ga.getAddAnswer(str, mv_num);
        }else if(mode == 2){
            ga.getRemoveAnswer(str, mv_num);
        }else{
            ga.getMoveAnswer(str, mv_num);
        }
        HashSet<String> ans_hash = new HashSet<String>(ga.ans_list);
        ArrayList<String> ans_with_no_duplicate = new ArrayList<String>(ans_hash);
        return ans_with_no_duplicate;
    }
}
