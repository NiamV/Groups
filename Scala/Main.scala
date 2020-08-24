object Main{
    def main(args: Array[String]): Unit = {
        // val e = Element("e")
        // val r = Element("r")
        // val rr = Element("rr")
        // val s = Element("s")
        // val rs = Element("rs")
        // val rrs = Element("rrs")

        // val ct = Array(
        //     Array(e, r, rr, s, rs, rrs),
        //     Array(r, rr, e, rs, rrs, s),
        //     Array(rr, e, r, rrs, s, rs),
        //     Array(s, rrs, rs, e, rr, r),
        //     Array(rs, s, rrs, r, e, rr),
        //     Array(rrs, rs, s, rr, r, e)
        // )

        // val d6elems = Array(e, r, rr, s, rs, rrs)
        // val d6ct = new CayleyTable(ct)
        // val d6gens = Array(r, s)

        // val d6 = new Group(d6elems, d6ct, d6gens)
        
        // val d6graph = new Graph(d6)

        // d6graph.top.visible = true

        val a = new Element("()")
        val b = new Element("(123)")
        val c = new Element("(124)")
        val d = new Element("(132)")
        val e = new Element("(134)")
        val f = new Element("(142)")
        val g = new Element("(143)")
        val h = new Element("(234)")
        val i = new Element("(243)")
        val j = new Element("(12)(34)")
        val k = new Element("(13)(24)")
        val l = new Element("(14)(23)")

        val ct = Array(
            Array(a, b, c, d, e, f, g, h, i, j, k, l),
            Array(b, d, k, a, h, g, l, j, c, e, i, f),
            Array(c, l, f, e, k, a, i, b, j, g, d, h),
            Array(d, a, i, b, j, l, f, e, k, h, c, g),
            Array(e, c, j, l, g, h, a, k, d, b, f, i),
            Array(f, h, a, k, d, c, j, l, g, i, e, b),
            Array(g, j, b, i, a, k, e, f, l, c, h, d),
            Array(h, k, e, f, l, j, b, i, a, d, g, c),
            Array(i, g, l, j, c, d, k, a, h, f, b, e),
            Array(j, i, h, g, f, e, d, c, b, a, l, k),
            Array(k, f, g, h, i, b, c, d, e, l, a, j),
            Array(l, e, d, c, b, i, h, g, f, k, j, a)
        )

        val a4elems = Array(a, b, c, d, e, f, g, h, i, j, k, l)
        val a4ct = new CayleyTable(ct)
        val a4gens = Array(b, j)

        val a4 = new Group(a4elems, a4ct, a4gens)
        val a4graph = new Graph(a4)
        a4graph.top.visible = true
    }
}