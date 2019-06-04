//Runtime: 0 ms, faster than 100.00% of Go online submissions for Merge Two Sorted Lists.
//Memory Usage: 2.5 MB, less than 87.98% of Go online submissions for Merge Two Sorted Lists.

/**
 * Definition for singly-linked list.
 * type ListNode struct {
 *     Val int
 *     Next *ListNode
 * }
 */
func mergeTwoLists(l1 *ListNode, l2 *ListNode) *ListNode {
    if l1 == nil{
		return l2
	}
	if l2 == nil{
		return l1
	}
	var l  *ListNode
	if l1.Val > l2.Val{
		l = l1
		l1 = l2
		l2 = l
	}
	l = l1
	for l2 != nil{
		if l1.Next == nil{
			l1.Next = l2
			break
		}
		if l2.Val <= l1.Next.Val{
			l3 := l1.Next
			l1.Next = l2
			l2 = l2.Next
			l1.Next.Next = l3
		}else{
			l1 = l1.Next
		}
	}
	return l
}