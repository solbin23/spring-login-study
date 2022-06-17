package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>(); //static사용
    private static long sequence = 0L;//static 사용

    public Member save(Member member) {
        member.setId(++sequence);
        log.info("save: member={}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String loginId) {//Optional = 껍데기통 -> Optional 안에 회원 객체가 있을 수 도 있고 없을 수 도 있다.
       /* List<Member> all = findAll();
        for (Member m : all) {
           if (m.getLoginId().equals(loginId)) {
               return Optional.of(m);
           }
        }
        return Optional.empty();*/

        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId)) //filter안의 조건에 만족하는 애들만 넘겨짐
                .findFirst();
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values()); //Member 가 List 로 변환
    }

    public void clearStore() {
        store.clear();
    }
}
