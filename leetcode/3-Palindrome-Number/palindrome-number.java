//Runtime: 8 ms, faster than 92.81% of Java online submissions for Palindrome Number.
//Memory Usage: 34.7 MB, less than 100.00% of Java online submissions for Palindrome Number.
class Solution {
    public boolean isPalindrome(int x) {
        if (x < 0) {
            return false;
        }
        ArrayList<Integer> inter = new ArrayList();
        while (x > 0) {
            inter.add(x % 10);
            x = x / 10;
        }
        for (int i = 0; i < inter.size() / 2; i++) {
            if (inter.get(i) != inter.get(inter.size() - i - 1)) {
                return false;
            }
        }
        return true;
    }
}