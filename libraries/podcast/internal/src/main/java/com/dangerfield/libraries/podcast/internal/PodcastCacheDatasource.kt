package com.dangerfield.libraries.podcast.internal

import com.dangerfield.libraries.coreflowroutines.AppScope
import com.dangerfield.libraries.podcast.PodcastShow
import com.dangerfield.libraries.podcast.storage.HeroImageDao
import com.dangerfield.libraries.podcast.storage.ItunesDataDao
import com.dangerfield.libraries.podcast.storage.PodcastEpisodeDao
import com.dangerfield.libraries.podcast.storage.PodcastShowDao
import com.dangerfield.libraries.podcast.storage.PodcastShowEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import podawan.core.Catching
import se.ansman.dagger.auto.AutoBind
import javax.inject.Inject

interface PodcastCacheDatasource {
    suspend fun getPodcastWithRssFeedLink(rssFeedLink: String): Catching<PodcastShow>
    suspend fun savePodcast(podcastShow: PodcastShow)
}

@AutoBind
class RoomPodcastCacheDatasource @Inject constructor(
    private val podcastDao: PodcastShowDao,
    private val heroImageDao: HeroImageDao,
    private val itunesDataDao: ItunesDataDao,
    private val episodeDao: PodcastEpisodeDao,
    @AppScope private val appScope: CoroutineScope
) : PodcastCacheDatasource {

    override suspend fun getPodcastWithRssFeedLink(rssFeedLink: String): Catching<PodcastShow> =
        Catching {
            val showEntity = podcastDao.getShowWithRssLink(rssFeedLink)
                ?: throw IllegalStateException("No show with rss feedLink: $rssFeedLink found in cache")

            val heroImageEntity = showEntity.heroImageId?.let { heroImageDao.getHeroImageById(it) }

            val itunesChannelDataEntity =
                showEntity.itunesChannelDataId?.let { itunesDataDao.getChannelDataById(it) }

            val items = episodeDao.getEpisodesForShow(showEntity.rssFeedLink).map { episodeEntity ->
                val itunesItemData = episodeEntity.itunesItemDataId?.let {
                    itunesDataDao.getEpisodeDataById(it)?.toDomain()
                }
                episodeEntity.toDomain(
                    itunesItemData = itunesItemData,
                    showHeroImage = heroImageEntity?.toDomain()
                )
            }

            PodcastShow(
                title = showEntity.title,
                link = showEntity.link,
                description = showEntity.description,
                image = heroImageEntity?.toDomain(),
                lastBuildDate = showEntity.lastBuildDate,
                updatePeriod = showEntity.updatePeriod,
                items = items,
                itunesChannelData = itunesChannelDataEntity?.toDomain(),
                rssFeedLink = showEntity.rssFeedLink
            )
        }

    override suspend fun savePodcast(podcastShow: PodcastShow) {
        appScope.launch {
            val heroImageId = podcastShow.image?.let { heroImageDao.insertHeroImage(it.toEntity()) }
            val itunesChannelDataId = podcastShow.itunesChannelData?.let { itunesDataDao.insertChannelData(it.toEntity()) }

            val showEntity = PodcastShowEntity(
                rssFeedLink = podcastShow.rssFeedLink,
                title = podcastShow.title,
                link = podcastShow.link,
                heroImageId = heroImageId,
                description = podcastShow.description,
                lastBuildDate = podcastShow.lastBuildDate,
                updatePeriod = podcastShow.updatePeriod,
                itunesChannelDataId = itunesChannelDataId
            )

            podcastDao.insertShow(showEntity)

            val episodeEntities = podcastShow.items.map { episode ->
                val itunesItemDataId = episode.itunesItemData?.let { itunesDataDao.insertEpisodeData(it.toEntity()) }
                episode.toEntity(
                    rssFeedLink = showEntity.rssFeedLink,
                    itunesItemDataId = itunesItemDataId,
                    showHeroImageId = heroImageId
                )
            }

            episodeDao.insertEpisodes(episodeEntities)
        }
    }
}