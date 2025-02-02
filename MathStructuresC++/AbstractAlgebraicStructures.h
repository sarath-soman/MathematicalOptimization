//
// Created by Sarath Soman on 23/09/2022.
//

#ifndef MATHSTRUCTURESC___ABSTRACTALGEBRAICSTRUCTURES_H
#define MATHSTRUCTURESC___ABSTRACTALGEBRAICSTRUCTURES_H

/// BinaryOperationWithoutClosure
/// \tparam T
/// \tparam R
template <typename T, typename R>
class BinaryOperationWithoutClosure {
public:
    /// Binary function without closure
    /// \param operand1
    /// \param operand2
    /// \return result of Type R
    virtual R op(const T& operand1, const T& operand2) const = 0;
};

/// BinaryOperation also known as Magma (T, op)
/// If op is partially defined then this structure is called a PartialMagma or PartialBinaryOperation
/// \tparam T
template <typename T>
class BinaryOperation: public BinaryOperationWithoutClosure<T, T>{
public:
    /// Binary function with closure
    /// a,b ∈ T then c = apply(a, b) ∈ T
    /// If apply is not defined for a, b ∈ T then apply is a partial binary operation
    /// \param operand1
    /// \param operand2
    /// \return result of Type T
    virtual T op(const T& operand1, const T& operand2) const = 0;
};

/// IAssociative represents the associativity property of operation op()
/// \tparam T
template <typename T>
class IAssociative: public virtual BinaryOperation<T>{
public:
    /// a,b,c ∈ T => op(op(a, b), c) = op(a, op(b, c))
    /// Associativity is closed downwards which means that if R ⊆ T then op is associative under R
    /// \tparam FN
    /// \param generator
    /// \return
//    template <typename FN>
//    bool isAssociative(FN& generator) {
//        std::cout << "FN& = " << std::endl;
//        return isAssociative(generator);
//    };

    template <typename FN>
    bool isAssociative(FN&& generator) const {
        auto a = generator();
        auto b = generator();
        auto c = generator();
        auto lhs = this->op(a, this->op(b, c));
        std::cout << "LHS = " << lhs << std::endl;
        auto rhs = this->op(this->op(a, b), c);
        std::cout << "RHS = " << rhs << std::endl;
        return  lhs == rhs;
    }
};

/// Semigroup has the following axioms
/// a,b,c ∈ T is Associative => op(op(a, b), c) = op(a, op(b, c))
/// Associativity is closed downwards which means that if R ⊆ T then op is associative under R
/// \tparam T
template <typename T>
class SemiGroup: public virtual BinaryOperation<T>, public IAssociative<T>{};

/// ICommutative represents the commutativity property of operation op()
/// \tparam T
template <typename T>
class ICommutative: public virtual BinaryOperation<T> {
public:
    /// a,b ∈ T => op(a, b) = op(b, a)
    /// \param generator
    /// \return
    bool isCommutative(const std::function<T()> generator) const {
        auto a = generator();
        auto b = generator();
        auto lhs = this->op(a, b);
        std::cout << "LHS = " << lhs << std::endl;
        auto rhs = this->op(b, a);
        std::cout << "RHS = " << rhs << std::endl;
        return  lhs == rhs;
    }
};

/// CommutativeSemiGroup has the following axioms
/// a,b,c ∈ T op is Associative => op(op(a, b), c) = op(a, op(b, c))
/// a,b ∈ T op is Commutative => op(a, b) = op(b, a)
/// Also known as AbelianSemiGroup
/// \tparam T
template <typename T>
class CommutativeSemiGroup: public SemiGroup<T>, public ICommutative<T>{};

/// Monoid has the following axioms
/// a,b,c ∈ T op is Associative => op(op(a, b), c) = op(a, op(b, c))
/// ∃e ∈ T, ∀a ∈ T a op e = e op a = a
/// \tparam T
template <typename T>
class Monoid: public SemiGroup<T> {
public:
    /// Identity element for a in T for the binary operation op(x, y)
    /// \return
    virtual T identity() const = 0;
};

/// CommutativeMonoid has the following axioms
/// a,b,c ∈ T op is Associative => op(op(a, b), c) = op(a, op(b, c))
/// a,b ∈ T op is Commutative => op(a, b) = op(b, a)
/// ∃e ∈ T, ∀a ∈ T a op e = e op a = a
/// \tparam T
template <typename T>
class CommutativeMonoid: public Monoid<T>, public ICommutative<T> {
public:
    /// Identity element for a in T for the binary operation op(x, y)
    /// \return
    virtual T identity() const = 0;
};

/// Group has the following axioms
/// a,b,c ∈ T op is Associative => op(op(a, b), c) = op(a, op(b, c))
/// ∃e ∈ T, ∀a ∈ T: a op e = e op a = a
/// ∀a ∈ T, ∃a' ∈ T: a op a' = e = a' op a. Where e is the identity element
/// \tparam T
template <typename T>
class Group: public Monoid<T> {
public:
    /// Inverse element for a in T for the binary operation op(x, y)
    /// \return
    virtual T inverse(T a) const = 0;
};

/// Also known as AbelianGroup
/// CommutativeGroup has the following axioms
/// a,b,c ∈ T op is Associative => op(op(a, b), c) = op(a, op(b, c))
/// a,b ∈ T op is Commutative => op(a, b) = op(b, a)
/// ∃e ∈ T, ∀a ∈ T: a op e = e op a = a
/// ∀a ∈ T, ∃a' ∈ T: a op a' = e = a' op a. Where e is the identity element
template <typename T>
class CommutativeGroup: public Group<T>, public ICommutative<T>{};

/// IDistributive represents the properties left distributivity & right distributivity of op2 over op1
/// \tparam T
template <typename T>
class IDistributive {
protected:
    const std::function<T(T, T)> op1;
    const std::function<T(T, T)> op2;

public:
    IDistributive(const std::function<T(T, T)>& op1, const std::function<T(T, T)>& op2): op1(op1), op2(op2) {}

    /// a, b, c int T is left distributive if (a op2 (b op1 c)) = ((a op2 b) op1 (a op2 c))
    /// \param generator
    /// \return
    bool isLeftDistributive(const std::function<T()>&& generator) const {
        T a = generator();
        T b = generator();
        T c = generator();
        T lhs = op2(a, op1(b, c));
        std::cout << "LHS = " << lhs << std::endl;
        T rhs = op1(op2(a, b), op2(a, c));
        std::cout << "RHS = " << rhs << std::endl;
        return lhs == rhs;
    }

    /// a, b, c int T is right distributive if ((b op1 c) op2 a) = ((b op2 a) op1 (c op2 a))
    /// \param generator
    /// \return
    bool isRightDistributive(const std::function<T()>&& generator) const {
        T a = generator();
        T b = generator();
        T c = generator();
        T lhs = op2(a, op1(b, c));
        std::cout << "LHS = " << lhs << std::endl;
        T rhs = op1(op2(b, a), op2(c, a));
        std::cout << "RHS = " << rhs << std::endl;
        return lhs == rhs;
    }
};

/// Ring is a triple (T, +, *), where T is a Set
/// Ring has the following axioms
/// (T, +) is a CommutativeGroup
/// (T, *) is a CommutativeMonoid
/// * is distributive over + (Both left & right)
/// \tparam T
template <typename T>
class Ring: public IDistributive<T>{
    const CommutativeGroup<T>& group;
    const Monoid<T>& monoid;

public:
    Ring(const CommutativeGroup<T>& group, const Monoid<T>& monoid): IDistributive<T>([&group](T x, T y) -> T {
        return group.op(x, y); //is there an elegant way | or a right way
    }, [&monoid](T x, T y) -> T {
        return monoid.op(x, y); //is there an elegant way | or a right way
    }), group(group), monoid(monoid) {
    }

};

/// CommutativeRing is a triple (T, +, *), where T is a Set
/// Ring has the following axioms
/// (T, +) is a CommutativeGroup
/// (T, *) is a CommutativeMonoid
/// * is distributive over + (In a commutative ring left distributivity implies right and vice versa)
/// \tparam T
template <typename T>
class CommutativeRing: public IDistributive<T>{
    const CommutativeGroup<T>& group;
    const CommutativeMonoid<T>& monoid;

public:
    CommutativeRing(const CommutativeGroup<T>& group, const CommutativeMonoid<T>& monoid): IDistributive<T>([&group](T x, T y) -> T {
        return group.op(x, y); //is there an elegant way | or a right way
    }, [&monoid](T x, T y) -> T {
        return monoid.op(x, y); //is there an elegant way | or a right way
    }), group(group), monoid(monoid) {
    }

};

/// SemiRing is a tripe (T, +, *), where T is a Set
/// SemiRing has the following axioms
/// (T, +) is a CommutativeMonoid, which means that it lacks being a CommutativeGroup because it is not having an additive
/// inverse.
/// (T, *) is a Monoid
/// a in T, a * 0 = 0 * a = 0 -> Multiplication by zero (CommutativeMonoid identity) annihilates a in T
template <typename T>
class SemiRing: public IDistributive<T>{
    const CommutativeMonoid<T>& commutativeMonoid;
    const Monoid<T>& monoid;

public:
    SemiRing(const CommutativeMonoid<T>& group, const Monoid<T>& monoid): IDistributive<T>([&group](T x, T y) -> T {
        return group.op(x, y); //is there an elegant way | or a right way
    }, [&monoid](T x, T y) -> T {
        return monoid.op(x, y); //is there an elegant way | or a right way
    }), commutativeMonoid(group), monoid(monoid) {}

    bool doesZeroAnnihilates(T a) const {
        return monoid.op(a, commutativeMonoid.identity()) == commutativeMonoid.identity() &&
            monoid.op(commutativeMonoid.identity(), a) == commutativeMonoid.identity();
    }

};

/// Field is a triple (T, +, *) with the following axioms
/// (T, +) is a CommutativeGroup, with identity element 0
/// (T*, *) is a CommutativeGroup, where T* = {a ∈ T | a ≠ 0 }, with identity element 1
/// 0 ≠ 1
/// \tparam T
template <typename T>
class Field: public IDistributive<T> {
    const CommutativeGroup<T>& additiveCommutativeGroup;
    const CommutativeGroup<T>& multiplicativeCommutativeGroup;

public:
    Field(const CommutativeGroup<T>& additiveCommutativeGroup, const CommutativeGroup<T>& multiplicativeCommutativeGroup): IDistributive<T>(
            [&additiveCommutativeGroup](T x, T y) ->T {
                return additiveCommutativeGroup.op(x, y);
            }, [&multiplicativeCommutativeGroup](T x, T y) ->T {
                return multiplicativeCommutativeGroup.op(x, y);
            }), additiveCommutativeGroup(additiveCommutativeGroup), multiplicativeCommutativeGroup(multiplicativeCommutativeGroup) {
        if (additiveCommutativeGroup.identity() == multiplicativeCommutativeGroup.identity()) {
            throw std::runtime_error("0 = 1. Additive identity cannot be equal to Multiplicative identity.");
        }
    }
};

#endif //MATHSTRUCTURESC___ABSTRACTALGEBRAICSTRUCTURES_H
