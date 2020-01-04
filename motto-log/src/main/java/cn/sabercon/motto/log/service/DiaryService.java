package cn.sabercon.motto.log.service;

import cn.sabercon.motto.common.dto.PageRes;
import cn.sabercon.motto.common.dto.PageReq;
import cn.sabercon.motto.common.util.EntityUtils;
import cn.sabercon.motto.log.dao.DiaryRepository;
import cn.sabercon.motto.log.entity.Diary;
import cn.sabercon.motto.log.util.LoginUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void save(Diary diaryInfo) {
        if (diaryInfo.getId() == null) {
            // create
            Diary diary = new Diary();
            EntityUtils.copyIgnoreNotCover(diaryInfo, diary);
            diary.setUserId(LoginUtils.getId());
            diary.setDel(0);
            repository.save(diary);
        } else {
            // update
            Diary diary = repository.getOne(diaryInfo.getId());
            EntityUtils.copyIgnoreNotCover(diaryInfo, diary);
        }
    }

    public void delete(Long id) {
        repository.getOne(id).setDel(1);
    }

    public Diary get(Long id) {
        return repository.getOne(id);
    }

    @Transactional(readOnly = true)
    public PageRes<Diary> list(PageReq pageReq) {
        pageReq.amendAll();
        Page<Diary> diaryPage;
        if (StringUtils.hasLength(pageReq.getEqualValue())) {
            // add query by type
            diaryPage = repository.findByUserIdAndNameLikeAndType(LoginUtils.getId(),
                    pageReq.getFuzzyValue(), pageReq.getEqualValue(), pageReq.toPageable());
        } else {
            diaryPage = repository.findByUserIdAndNameLike(LoginUtils.getId(), pageReq.getFuzzyValue(), pageReq.toPageable());
        }
        // no need to return text when getting page
        diaryPage.forEach(diary -> diary.setText(null));
        return PageRes.of(diaryPage);
    }

}
