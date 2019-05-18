//Runtime: 100 ms, faster than 38.58% of C# online submissions for Merge Two Sorted Lists.
//Memory Usage: 23.7 MB, less than 40.40% of C# online submissions for Merge Two Sorted Lists.


/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     public int val;
 *     public ListNode next;
 *     public ListNode(int x) { val = x; }
 * }
 */
public class Solution {
    public ListNode MergeTwoLists(ListNode l1, ListNode l2) {
            ListNode l;
            if (l1 == null)
            {
                return l2;
            }
            if (l2 == null)
            {
                return l1;
            }
            if(l1.val > l2.val)
            {
                l = l1;
                l1 = l2;
                l2 = l;
            }
            l = l1;
            while(l2 != null)
            {
                if (l1.next == null)
                {
                    l1.next = l2;
                    break;
                }
                if(l2.val <= l1.next.val)
                {
                    ListNode l3 = l1.next;
                    l1.next = l2;
                    l2 = l2.next;
                    l1.next.next = l3;
                }
                else
                {
                    l1 = l1.next;
                }
            }
            return l;
    }
}