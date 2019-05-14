//Runtime: 2 ms, faster than 81.36% of Java online submissions for Valid Parentheses.
//Memory Usage: 33.1 MB, less than 100.00% of Java online submissions for Valid Parentheses.
class Solution {
    public boolean isValid(String s) {
        Stack<Character> st = new Stack<>();
        if(s.length()==0){
            return true;
        }
        if (s.length()%2==1){
            return false;
        }
        if(s.charAt(0)=='}'||s.charAt(0)==')'||s.charAt(0)==']'){
            return false;
        }
        for (int i=0;i<s.length();i++){
            if(s.charAt(i)=='['||s.charAt(i)=='('||s.charAt(i)=='{'){
                st.push(s.charAt(i));
            }
            if(s.charAt(i)==')'&&st.pop()!='('){
                return false;
            }
            if(s.charAt(i)==']'&&st.pop()!='['){
                return false;
            }
            if(s.charAt(i)=='}'&&st.pop()!='{'){
                return false;
            }
        }
        if(!st.empty()){
            return false;
        }
        return true;

    }
}