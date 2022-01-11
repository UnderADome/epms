package com.wisdri.epms.Service;

import com.wisdri.epms.Dao.MeetingMapper;
import com.wisdri.epms.Entity.Meeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeetingService {
    @Autowired
    private MeetingMapper meetingMapper;
}
