> import Data.List
> import Data.Maybe

> type CayleyTable a = [[a]]

Type class that can be used to create a group based on elements and a Cayley table

> class Group a where
>	identity	:: a
>	multiply	:: a -> a -> a
>	inverse		:: a -> a

Generic function that multiplies two elements with a Cayley table

> mult :: Eq a => a -> a -> (CayleyTable a) -> a
> mult g h ct = (ct !! (fromJust (elemIndex g elems)) ) !! (fromJust (elemIndex h elems))
>	where elems = ct !! 0

Generic function that finds the inverse of an element from a Cayley table

> inv :: Eq a => a -> a -> (CayleyTable a) -> a
> inv g id ct = (ct !! 0) !! (fromJust ( elemIndex id (map action (ct !! 0)) ) )
>	where action h = (mult g h ct)

---------------------------------------------------------------------------------

Collapsing a product of elements into on element using a group:

> data Element a = Elem a | Product (Element a) (Element a)
>	deriving Show

> collapse :: Group a => Element a -> a
> collapse (Elem a) = a
> collapse (Product leftElem rightElem) = multiply (collapse leftElem) (collapse rightElem)

---------------------------------------------------------------------------------

D6 group:

> data D6 = E | R | RR | S | RS | RRS
>   deriving (Eq, Show)

> d6CayleyTable :: CayleyTable D6
> d6CayleyTable = [   [E, R, RR, S, RS, RRS],
>                     [R, RR, E, RS, RRS, S],
>                     [RR, E, R, RRS, S, RS],
>                     [S, RRS, RS, E, RR, R],
>                     [RS, S, RRS, R, E, RR],
>                     [RRS, RS, S, RR, R, E]  ]

> instance Group D6 where
>	identity = E
>	multiply g h = mult g h d6CayleyTable
>	inverse g = inv g E d6CayleyTable 

