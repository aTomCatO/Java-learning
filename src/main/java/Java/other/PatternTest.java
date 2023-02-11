package Java.other;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTest {
    String context = "2001-10-15";
    @Test
    public void test1(){
        String regex = "(\\d{4})-(\\d{2})-(\\d{2})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(context);
        while (matcher.find()){
            System.out.println(matcher.group(0)+"***"+matcher.group(1)+"***"+matcher.group(2)+"***"+matcher.group(3));
        }
    }
    @Test
    public void test2(){
        String regex = "\\d{4}|\\d{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(context);
        while (matcher.find()){
            System.out.println(matcher.group(0));
        }
    }

    @Test
    public void test3(){
        //Pattern.CASE_INSENSITIVE表示匹配是不区分字母大小写的
        String words = "eager a.渴望的，热切的 early ad.早 a.早的，早期的 earn vt.赚得，挣得；获得 earnest a.认真的，诚恳的 earthquake n.地震；大震荡 ease n.容易，舒适 vt.缓和 easily ad.容易地；舒适的 eastern a.东方的；朝东的 echo n.回声，反响 vi.重复 economic a.经济的，经济学的 economical a.节约的；经济学的 economy n.经济；节约，节省 editor n.编辑，编者，校订者 educate vt.教育；培养；训练 education n.教育；训导；教育学 effect n.结果；效果，效力 effective a.有效的；有影响的 efficiency n.效率；功效，效能 efficient a.效率高的，有能力的 effort n.努力；努力的成果 elaborate a.复杂的；精心制作的 elastic n.松紧带 a.有弹性的 elbow n.肘，肘部；弯管 elect vt.选举，推选；选择 election n.选举，选择权；当选 electric a.电的，电动的 electrical a.电的，电气科学的 electricity n.电，电学；电流 electron n.电子 electronic a.电子的 electronics n.电子学 element n.成分；要素；元素 elementary a.基本的；初级的 elephant n.象 elevator n.电梯；升降机 eliminate vt.消灭，消除，排除 elimination n.消灭，排除，消除 else ad.其它，另外 a.别的 elsewhere ad.在别处，向别处 embarrass vt.使窘迫，使为难 embrace vt.拥抱；包括；包围 emerge vi.出现，涌现；冒出 emergency n.紧急情况，突然事件 emit vt.散发；发射；发表 emotional a.感情的，情绪的 emperor n.皇帝 emphasis n.强调，重点，重要性 emphasize vt.强调，着重 empire n.帝国 employ vi.雇用；用；使忙于 employee n.受雇者，雇员，雇工 employer n.雇佣者，雇主 employment n.工业；雇用；使用 enclose vt.围住，圈起；附上 encounter vt.遭遇，遇到 n.遭遇 encourage vt.鼓励，支持，助长 endure vt.忍受；容忍 enemy n.敌人；仇敌；敌兵 energy n.活力；精力；能 enforce vt.实施，执行；强制 engage vt.使从事于；聘用 engine n.发动机，引擎；机车 engineer n.工程师，技师 engineering n.工程，工程学 enlarge vt.扩大，扩展；放大 enormous a.巨大的，庞大的 ensure vt.保证；保护；赋予 entertain vt.使欢乐；招待 enthusiasm n.热情，热心，热忱 enthusiastic a.热情的，热心的 entire a.全部的，整个的 entitle vt.给…权利(或资格) entrance n.入口，门口；进入 entry n.入口处；登记；进入 envelope n.信封；封套；封皮 environment n.环境，外界；围绕 envy vt.&n.妒忌；羡慕 equal a.相等的；平等的 equality n.等同，平等；相等 equation n.方程(式)；等式 equip vt.装备，配备 equipment n.装备，设备，配备 equivalent a.相等的；等量的 era n.时代，年代；纪元 erect vt.建造；使竖立 error n.错误，谬误；差错 escape vi.逃跑；逸出 n.逃跑 especially ad.特别，尤其，格外 establish vt.建立，设立；确立 establishment n.建立，设立，确立 estimate vt.估计，评价 n.估计 European a.欧洲的 n.欧洲人 evaluate vt.评价，估…的价 evaporate vt.使蒸发 vi.蒸发 eve n.前夜，前夕，前一刻 even ad.甚至；甚至更，还 even a.均匀的；平的 event n.事件，大事；事变 eventually ad.终于；最后 ever ad.在任何时候；曾经 evidence n.根据；证据，证人 evident a.明显的，明白的 evil n.邪恶；祸害 a.坏的 evolution n.进化，演化；发展 evolve vt.使进化；使发展 exactly ad.确切地；恰恰正是 exaggerate vt.&vi.夸大，夸张 examination n.考试；检查，细查 examine vt.检查，仔细观察 example n.例子，实例；模范 exceed vt.超过，胜过；超出 exceedingly ad.极端地，非常 excellent a.优秀的，杰出的 except prep.除…之外 exception n.例外，除外 excess n.超越；过量；过度 excessive a.过多的，极度的 exchange vt.交换；交流 n.交换 excite vt.使激动；引起 exciting a.令人兴奋的 exclaim vi.呼喊；惊叫 exclude vt.把…排除在外 exclusively ad.专门地 excursion n.远足；短途旅行 excuse vt.原谅；免除 n.借口 exercise n.锻炼，训练 vi.练习 exhaust vt.使筋疲力尽；用尽 exhibit vt.显示；陈列，展览 exhibition n.展览，陈列；展览会 exist vi.存在；生存，生活 existence n.存在，实在；生存 exit n.出口；退场 vi.退出 expand vt.扩大；使膨胀 expansion n.扩大，扩充；扩张 expect vt.预料，预期；等待 expectation n.期待，期望，预期 expense n.花费，消费；费用 expensive a.昂贵的，花钱多的 experience n.经验，感受；经历 experiment n.实验；试验 experimental a.实验的，试验的 expert n.专家 a.熟练的 explain vt.解释；为…辩解 explanation n.解释，说明；辩解 explode vt.使爆炸 vi.爆炸 explore vt.&vi.探险，探索 explosion n.爆炸，爆发，炸裂 export vt.输出，出口；运走 expose vt.使暴露；揭露 exposure n.暴露；揭露；曝光 express vt.表示 n.快车，快递 expression n.词句；表达；表情 extend vt.延长；扩大；致 extension n.延长部分；伸展 extensive a.广阔的；广泛的 extent n.广度；范围；程度 exterior a.外部的；对外的 external a.外部的，外面的 extra a.额外的 ad.特别地 extraordinary a.非同寻常的，特别的 extreme a.极度的；尽头的 extremely ad.极端，极其，非常 eyesight n.视力，目力";
        String regex = "([a-zA-Z]+)\\s([a-zA-Z&.]+\\.[\u4e00-\u9fa5\\pP]+(\\s[a-zA-Z&.]+\\.[\u4e00-\u9fa5\\pP]+)*)";
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(words);
        while (matcher.find()) {
            String element = matcher.group(0);
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
            System.out.println(element);
        }
    }
}
