import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Java8StreamTest {

    class ObjectA {
        Integer idx;
        String name;
        String addr;

        public ObjectA(Integer idx, String name, String addr) {
            this.idx = idx;
            this.name = name;
            this.addr = addr;
        }

        public Integer getIdx() {
            return idx;
        }

        public String getName() {
            return name;
        }

        public String getAddr() {
            return addr;
        }
    }

    @Test
    public void streamListToListtest_01 () {
        List<ObjectA> objectAList = new ArrayList<>();
        objectAList.add(new ObjectA(1, "name1", "addr1"));
        objectAList.add(new ObjectA(2, "name2", "addr2"));
        objectAList.add(new ObjectA(3, "name3", "addr3"));
        objectAList.add(new ObjectA(4, "name4", "addr4"));
        List<Integer> idxList = objectAList.stream().map(ObjectA::getIdx).collect(Collectors.toList());
        System.out.println(idxList.toString());
    }
}
