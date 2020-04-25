class UsingGenericsForLinkedList[X] {
    private class Node[X](elem: X) {
        var next: Node[X] = _
        override def toString = elem.toString 
    }

    private var head: Node[X] = _

    def add(elem: X) { // 링크드 리스트에 엘리먼트를 추가한다.
        val value = new Node(elem)
        value.next = head
        head = value
    }

    private def printNodes(value: Node[X]) { // 노드의 값들을 출력한다. 
        if ( value != null ) {
            println(value)
            printNodes(value.next)
        }
    }
    def printAll() { printNodes(head) }
}


object UsingGenericsForLikedList {
    def main(args: Array[String]) {
        val ints = new UsingGenericsForLinkedList[Int]()
        ints.add(1)
        ints.add(2)
        ints.add(3)
        ints.printAll()

        // 클래스는 Generic 타입을 사용하기 때문에 String 타입의 링크드 리스트를 생성할 수도 있다.
        val strings = new UsingGenericsForLinkedList[String]() 
        strings.add("Salman")
        strings.add("Xamir")
        strings.add("Shah Rukh")
        strings.printAll()

        val doubles = new UsingGenericsForLinkedList[Double]()
        doubles.add(10.50)
        doubles.add(27.75)
        doubles.add(12.90)
        doubles.printAll()
    }


}