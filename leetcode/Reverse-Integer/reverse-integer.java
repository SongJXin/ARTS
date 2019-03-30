//Runtime: 2 ms, faster than 99.41% of Java online submissions for Reverse Integer.
//Memory Usage: 32.6 MB, less than 100.00% of Java online submissions for Reverse Integer.

class Solution {
    public int reverse(int x) {
        boolean minus = false;
        if (x < 0){
            minus = true;
        }
        Queue<Integer> inter=new LinkedList<Integer>();
        while (x != 0)
        {
            if (minus) {
                inter.offer(- (x % 10));
            }
            else {
                inter.offer(x % 10);
            }
            x = x / 10;
        }
        int result = 0;
        while (!inter.isEmpty()) {
            result = result * 10 + inter.poll();
            if (inter.size() == 1) {
                if (result > 214748364) {
                    return 0;
                }
                else if (result == 214748364) {
                    if (minus && inter.peek() >= 8) {
                        return 0;
                    }
                    if (inter.peek() >= 7) {
                        return 0;
                    }
                }
            }
        }
        if (minus) {
            result = -result;
        }
        return result;
    }
}