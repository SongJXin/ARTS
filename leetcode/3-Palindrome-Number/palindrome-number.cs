#Runtime: 88 ms, faster than 72.86% of C# online submissions for Palindrome Number.
#Memory Usage: 16.9 MB, less than 19.78% of C# online submissions for Palindrome Number.
public class Solution {
    public bool IsPalindrome(int x) {
        if (x < 0){
            return false;
        }
        ArrayList inter = new ArrayList();
        while(x > 0){
            inter.Add(x % 10);
            x = x / 10;
        }
        for(int i = 0; i < inter.Count/2; i++){
            if (!inter[i].Equals(inter[inter.Count - i - 1])){
                return false;
            }
        }
        return true;
    }
}