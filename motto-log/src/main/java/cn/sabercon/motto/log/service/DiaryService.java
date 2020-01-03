package cn.sabercon.motto.log.service;

import cn.sabercon.motto.common.dto.CommonPage;
import cn.sabercon.motto.common.dto.PageReq;
import cn.sabercon.motto.common.util.EntityUtils;
import cn.sabercon.motto.log.component.OssHelper;
import cn.sabercon.motto.log.dao.DiaryRepository;
import cn.sabercon.motto.log.dto.DiaryDto;
import cn.sabercon.motto.log.entity.Diary;
import cn.sabercon.motto.log.util.LoginUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author ywk
 * @date 2020-01-03
 */
@Service
@Transactional
public class DiaryService {

    @Autowired
    private DiaryRepository repository;
    @Autowired
    private OssHelper ossHelper;
    @Value("${aliyun.oss.dir.diaryImg}")
    private String imgPath;

    public void save(DiaryDto dto) {
        if (dto.getId() == null) {
            // create
            Diary diary = new Diary();
            EntityUtils.copyIgnoreEmpty(dto, diary);
            diary.setUserId(LoginUtils.getId());
            diary.setDel(0);
            repository.save(diary);
        } else {
            // update
            Diary diary = repository.getOne(dto.getId());
            EntityUtils.copyIgnoreEmpty(dto, diary);
        }
    }

    public void delete(Long id) {
        Diary diary = repository.getOne(id);
        diary.setDel(1);
    }

    public DiaryDto get(Long id) {
        Diary diary = repository.getOne(id);
        return toDto(diary);
    }

    public CommonPage<DiaryDto> list(PageReq pageReq) {
        pageReq.amendAll();
        Page<Diary> diaryPage;
        if (StringUtils.hasLength(pageReq.getEqualValue())) {
            // add query by type
            diaryPage = repository.findByUserIdAndNameLikeAndType(LoginUtils.getId(),
                    pageReq.getFuzzyValue(), pageReq.getEqualValue(), pageReq.getPageable());
        } else {
            diaryPage = repository.findByUserIdAndNameLike(LoginUtils.getId(), pageReq.getFuzzyValue(), pageReq.getPageable());
        }
        return CommonPage.of(diaryPage.map(this::toDtoWithoutText));
    }

    private DiaryDto toDto(Diary diary) {
        DiaryDto diaryDto = new DiaryDto();
        BeanUtils.copyProperties(diary, diaryDto);
        return diaryDto;
    }

    /**
     * 删除日记的内容，用于获取列表
     */
    private DiaryDto toDtoWithoutText(Diary diary) {
        DiaryDto diaryDto = toDto(diary);
        diaryDto.setText(null);
        return diaryDto;
    }
}
