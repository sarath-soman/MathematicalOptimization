package org.sarath;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Semigroup is a Pair<T, op> with the following axioms
 * 1. Closure property -> op is closed under T
 * 2. Left and Right Associative property -> a,b,c ∈ T is Associative => op(op(a, b), c) = op(a, op(b, c))
 *    Associativity is closed downwards which means that if R ⊆ T then op is associative under R
 */
record SemiGroup<T>(BiFunction<T, T, T> op){}

/**
 * CommutativeSemigroup is a Pair<T, op> with the following axioms
 * 1. Closure property -> op is closed under T
 * 2. Left and Right Associative property -> a,b,c ∈ T is Associative => op(op(a, b), c) = op(a, op(b, c))
 *    Associativity is closed downwards which means that if R ⊆ T then op is associative under R
 * 3. Commutative property -> a,b ∈ T op is Commutative => op(a, b) = op(b, a)
 */
record CommutativeSemiGroup<T>(BiFunction<T, T, T> op){}

/**
 * Monoid is a Pair<T, op> with the following axioms
 * 1. Closure property -> op is closed under T
 * 2. Left and Right Associative property -> a,b,c ∈ T is Associative => op(op(a, b), c) = op(a, op(b, c))
 *    Associativity is closed downwards which means that if R ⊆ T then op is associative under R
 * 3. Identity property -> ∃e ∈ T, ∀a ∈ T => a op e = e op a = a
 */
record Monoid<T>(BiFunction<T, T, T> op, T identity){}

/**
 * CommutativeMonoid is a Pair<T, op> with the following axioms
 * 1. Closure property -> op is closed under T
 * 2. Left and Right Associative property -> a,b,c ∈ T is Associative => op(op(a, b), c) = op(a, op(b, c))
 *    Associativity is closed downwards which means that if R ⊆ T then op is associative under R
 * 3. Commutative property -> a,b ∈ T op is Commutative => op(a, b) = op(b, a)
 * 4. Identity property -> ∃e ∈ T, ∀a ∈ T => a op e = e op a = a
 */
record CommutativeMonoid<T>(BiFunction<T, T, T> op, T identity){}


/**
 * Group is a Pair<T, op> with the following axioms
 * 1. Closure property -> op is closed under T
 * 2. Left and Right Associative property -> a,b,c ∈ T is Associative => op(op(a, b), c) = op(a, op(b, c))
 *    Associativity is closed downwards which means that if R ⊆ T then op is associative under R
 * 3. Identity property -> ∃e ∈ T, ∀a ∈ T: a op e = e op a = a
 * 4. Inverse property -> ∀a ∈ T, ∃a' ∈ T: a op a' = e = a' op a, Where e is the identity element
 */
record Group<T>(BiFunction<T, T, T> op, T identity, Function<T, T> inverse){}

/**
 * CommutativeGroup is a Pair<T, op> with the following axioms.
 * Commutative group is also known as Abelian group.
 * 1. Closure property -> op is closed under T
 * 2. Left and Right Associative property -> a,b,c ∈ T is Associative => op(op(a, b), c) = op(a, op(b, c))
 *    Associativity is closed downwards which means that if R ⊆ T then op is associative under R
 * 3. Commutative property -> a,b ∈ T op is Commutative => op(a, b) = op(b, a)
 * 4. Identity property -> ∃e ∈ T, ∀a ∈ T => a op e = e op a = a
 * 5. Inverse property -> ∀a ∈ T, ∃a' ∈ T: a op a' = e = a' op a, Where e is the identity element
 */
record CommutativeGroup<T>(BiFunction<T, T, T> op, T identity, Function<T, T> inverse){}

/**
 * Ring is a Triplet<T, +, *> with the following axioms
 * 1. Pair<T, +> is a CommutativeGroup
 * 2. Pair<T, *> is a Monoid
 * 3. * is both left and right distributive over +
 *
 * Right distributivity property -> a, b, c ∈ T: ((b + c) * a) = ((b * a) + (c * a))
 * Left distributivity property -> a, b, c ∈  T: (a * (b + c)) = ((a * b) + (a * c))
 */
record Ring<T>(CommutativeGroup<T> group, Monoid<T> monoid){}

/**
 * CommutativeRing is a Triplet<T, +, *> with the following axioms
 * 1. Pair<T, +> is a CommutativeGroup
 * 2. Pair<T, *> is a CommutativeMonoid
 * 3. * is both left and right distributive over +
 *
 * Right distributivity property -> a, b, c ∈ T: ((b + c) * a) = ((b * a) + (c * a))
 * Left distributivity property -> a, b, c ∈  T: (a * (b + c)) = ((a * b) + (a * c))
 */
record CommutativeRing<T>(CommutativeGroup<T> group, CommutativeMonoid<T> monoid){}

/**
 * SemiRing is a Triplet<T, +, *> with the following axioms
 * 1. Pair<T, +> is a CommutativeMonoid
 * 2. Pair<T, *> is a Monoid
 * 3. * is both left and right distributive over +
 *
 * Right distributivity property -> a, b, c ∈ T: ((b + c) * a) = ((b * a) + (c * a))
 * Left distributivity property -> a, b, c ∈  T: (a * (b + c)) = ((a * b) + (a * c))
 */
record SemiRing<T>(CommutativeMonoid<T> group, Monoid<T> monoid){}

/**
 * Field is a Triplet<T, +, *> with the following axioms
 * 1. Pair<T, +> is a CommutativeGroup
 * 2. Pair<Ts, *> is a CommutativeGroup, where Ts = {a ∈ T | a ≠ 0 }, with identity element 1
 * 3. * is both left and right distributive over +
 * 4. 0 ≠ 1
 *
 * Right distributivity property -> a, b, c ∈ T: ((b + c) * a) = ((b * a) + (c * a))
 * Left distributivity property -> a, b, c ∈  T: (a * (b + c)) = ((a * b) + (a * c))
 */
record Field<T, Ts>(CommutativeGroup<T> additiveGroup, CommutativeGroup<Ts> multiplicativeGroup){}

/**
 * Vector space is a Tuple<V, F, +, *>, * is the scalar multiplication operation and + is vector addition operation,
 * and V is an n-tuple. Example R^n, n elements of R - (r1, r2, .., rn)
 * Vector space has the following axioms
 * 1. Pair<V, +> is a commutative group
 * 2. Closure property for * -> ∀x ∈ F and v ∈ V: x * v ∈ V
 * 3. ∀z ∈ F: 1 * z = z , where 1 is the multiplicative identity of F
 * 4. Associative property for * -> ∀x,y ∈ F and v ∈ V: x * y * (v) = x * (y * v)
 * 5. Distributive property of one scalar over two vectors -> ∀x ∈ F and k, j ∈ V: x * (k + j) = x * k + x * j
 * 6. Distributive property of two scalars over one vector -> ∀x,y ∈ F and v ∈ V: (x + y) * v = x * v + y + v
 *
 * Subspace
 * ---------------------------------------------------------------------------------------------------------------------
 * U is a subspace of V denoted by ≤ as  U ≤  V, if U ⊆ V
 * All universal statements about V are true for U as well, in other words the universal quantification is closed downwards.
 *
 * Bases
 * ---------------------------------------------------------------------------------------------------------------------
 * let v, w ∈ V and j, k ∈ F then the expression jv + kw is called a linear combination of v and w. The scalars j and k
 * are called weights.
 *
 * Span:
 * If v, w ∈ V then the set of all linear combination of v and w is called the span of v and w.
 * span{v, w} = {jv + kw | j, k ∈ F}
 *
 * Linear Independence:
 * If v, w ∈ V then we say that v and w are linearly independent if neither of the vectors are a scalar multiple of the other.
 * If one of them is a scalar multiple of the other, then we say that they are linearly dependent.
 *
 * Basis:
 * If v, w ∈ V then we say that {v, w} is a basis if v and w are linearly independent and span{v, w} = V
 *
 */
record VectorSpace<V, F>(CommutativeGroup<V> commutativeGroup, Field<F, F> field, BiFunction<V, V, V> plus, BiFunction<F, V, V> multiply){}
