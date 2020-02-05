package run.halo.app.controller.content;

import cn.hutool.core.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import run.halo.app.model.entity.Post;
import run.halo.app.model.enums.PostStatus;
import run.halo.app.model.vo.PostListVO;
import run.halo.app.service.OptionService;
import run.halo.app.service.PostService;
import run.halo.app.service.ThemeService;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @Description TODO
 * @Author lmf.
 * @Date 2020/1/16
 */
@Slf4j
@Controller
@RequestMapping(value = "/time-line")
public class ContentTimeLineController {

    @Autowired
    private OptionService optionService;
    @Autowired
    private PostService postService;
    @Autowired
    private ThemeService themeService;


    /**
     * Render post archives page.
     *
     * @param model model
     * @return template path : themes/{theme}/archives.ftl
     */
    @GetMapping
    public String timeLines(Model model) {
        return this.timeLine(model, 1, Sort.by(DESC, "createTime"));
    }

    /**
     * Render post archives page.
     *
     * @param model model
     * @return template path : themes/{theme}/archives.ftl
     */
    public String timeLine(Model model,
                           @PathVariable(value = "page") Integer page,
                           @SortDefault(sort = "createTime", direction = DESC) Sort sort) {
        Pageable pageable = PageRequest.of(page - 1, 25, sort);

        Page<Post> postPage = postService.pageBy(PostStatus.PUBLISHED, pageable);
        Page<PostListVO> postListVos = postService.convertToListVo(postPage);
        int[] pageRainbow = PageUtil.rainbow(page, postListVos.getTotalPages(), 3);

        model.addAttribute("is_archives", true);
        model.addAttribute("pageRainbow", pageRainbow);
        model.addAttribute("posts", postListVos);

        return themeService.render("time-line");
    }

}
