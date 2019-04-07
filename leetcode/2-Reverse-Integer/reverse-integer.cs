# Runtime: 40 ms, faster than 100.00% of C# online submissions for Reverse Integer.
# Memory Usage: 13.4 MB, less than 15.84% of C# online submissions for Reverse Integer.
public class Solution {
    public int Reverse(int x) {
        bool minus = false;
            Queue inter = new Queue();
            if (x < 0)
            {
                minus = true;
            }
            while (x != 0)
            {
                if (minus)
                {
                    inter.Enqueue(-(x % 10));
                }
                else
                {
                    inter.Enqueue(x % 10);
                }
                x = x / 10;
            }
            int result = 0;
            while (inter.Count != 0)
            {
                result = result * 10 + (int)inter.Dequeue();
                if (inter.Count == 1)
                {
                    if (result > 214748364)
                    {
                        return 0;
                    }
                    else if (result == 214748364)
                    {
                        int front = (int)inter.Dequeue();
                        if (minus && front >= 8)
                        {
                            return 0;
                        }
                        if (front >= 7)
                        {
                            return 0;
                        }
                        inter.Enqueue(front);
                    }
                }
            }
            if (minus)
            {
                result = -result;
            }
            return result;
    }
}