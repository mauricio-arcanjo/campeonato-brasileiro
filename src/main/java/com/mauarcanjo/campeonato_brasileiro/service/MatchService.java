package com.mauarcanjo.campeonato_brasileiro.service;

import com.mauarcanjo.campeonato_brasileiro.dto.match.AddMatchDto;
import com.mauarcanjo.campeonato_brasileiro.dto.match.DetailsMatchDto;

public interface MatchService {
    DetailsMatchDto addMatch(AddMatchDto addMatchDto);
}
